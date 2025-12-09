package cat.uvic.teknos.dam.aeroadmin.client;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.CryptoUtils;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.EncryptionUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class PersistentAirlineApiClient {

    private final String host;
    private final int port;
    private Socket socket;
    private final RawHttp http;
    private final Gson gson;

    private static final String HASH_HEADER = "X-Message-Hash";

    private SecretKey sessionKey;
    private final String clientId;

    private Timer inactivityTimer;
    private final long INACTIVITY_TIMEOUT_MS = 2 * 60 * 1000;

    public PersistentAirlineApiClient(String baseUrl, String clientId) {
        URI uri = URI.create(baseUrl);
        this.host = uri.getHost();
        this.port = (uri.getPort() == -1) ? 80 : uri.getPort();
        this.http = new RawHttp();
        this.gson = new Gson();
        this.clientId = clientId;
    }

    public void connect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            return;
        }
        this.socket = new Socket(host, port);
        System.out.println("🌐 Connectat al servidor " + host + ":" + port);

        try {
            doSecureHandshake();
        } catch (Exception e) {
            throw new IOException("❌ Error en el handshake de seguretat: " + e.getMessage(), e);
        }

        resetInactivityTimer();
    }

    private void doSecureHandshake() throws Exception {
        System.out.println("🔐 Iniciant handshake com a: " + clientId);

        RawHttpRequest request = http.parseRequest(
                "GET /keys/" + clientId + " HTTP/1.1\r\n" +
                        "Host: " + host + "\r\n" +
                        "Connection: keep-alive\r\n");
        request.writeTo(socket.getOutputStream());

        RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
        if (response.getStatusCode() != 200) {
            throw new IOException("Handshake fallit (" + response.getStatusCode() + "). El servidor no coneix al client: " + clientId);
        }

        String jsonBody = response.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(jsonBody).getAsJsonObject();
        String encryptedKey = json.get("key").getAsString();

        String keystorePath = "/" + clientId + ".jks";

        if (getClass().getResourceAsStream(keystorePath) == null) {
            throw new IOException("No s'ha trobat el fitxer de claus: " + keystorePath + " a resources.");
        }

        KeyStore clientKs = EncryptionUtils.loadKeyStore(keystorePath, "123456");
        PrivateKey myPrivateKey = (PrivateKey) clientKs.getKey(clientId, "123456".toCharArray());

        this.sessionKey = EncryptionUtils.decryptRSA(encryptedKey, myPrivateKey);
        System.out.println("✅ Sessió segura establerta (AES-256).");
    }

    public void disconnect() throws IOException {
        if (socket == null || socket.isClosed()) return;

        if (inactivityTimer != null) {
            inactivityTimer.cancel();
            inactivityTimer = null;
        }

        try {
            RawHttpRequest request = http.parseRequest(
                    "GET /disconnect HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "Connection: keep-alive\r\n"
            );
            request.writeTo(socket.getOutputStream());
            http.parseResponse(socket.getInputStream()).eagerly();
        } catch (Exception e) {
        } finally {
            if (socket != null) socket.close();
            System.out.println("🔌 Connexió tancada.");
        }
    }

    private void resetInactivityTimer() {
        if (inactivityTimer != null) inactivityTimer.cancel();
        inactivityTimer = new Timer(true);
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("\n[TIMER] Temps d'espera esgotat. Desconnectant...");
                try {
                    disconnect();
                } catch (IOException e) {
                    System.err.println("Error desconnectant: " + e.getMessage());
                }
            }
        }, INACTIVITY_TIMEOUT_MS);
    }

    private RawHttpResponse<?> sendPersistentRequest(RawHttpRequest request) throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Connexió perduda. Reconnectant...");
            connect();
        }
        resetInactivityTimer();

        if (sessionKey != null && request.getBody().isPresent()) {
            try {
                String plainBody = request.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);

                String encryptedBody = EncryptionUtils.encryptAES(plainBody, sessionKey);
                byte[] encryptedBytes = encryptedBody.getBytes(StandardCharsets.UTF_8);
                String newHash = CryptoUtils.hash(encryptedBytes);

                RawHttpHeaders newHeaders = RawHttpHeaders.newBuilder()
                        .merge(request.getHeaders())
                        .overwrite(HASH_HEADER, newHash)
                        .remove("Content-Length")
                        .build();

                request = request.withBody(new StringBody(encryptedBody))
                        .withHeaders(newHeaders);

            } catch (Exception e) {
                throw new IOException("Error encriptant petició", e);
            }
        }

        System.out.println("Enviant petició: " + request.getMethod() + " " + request.getUri());
        request.writeTo(socket.getOutputStream());

        RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();

        if (sessionKey != null && response.getBody().isPresent()) {
            try {
                String encryptedResp = response.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
                String decryptedResp = EncryptionUtils.decryptAES(encryptedResp, sessionKey);

                response = response.withBody(new StringBody(decryptedResp));

            } catch (Exception e) {
                throw new IOException("Error desencriptant resposta: " + e.getMessage(), e);
            }
        }

        if (response.getStatusCode() >= 300) {
            String errorBody = response.getBody().map(b -> {
                try { return b.decodeBodyToString(StandardCharsets.UTF_8); }
                catch (IOException e) { return ""; }
            }).orElse("");
            throw new IOException("Error servidor: " + response.getStatusCode() + " - " + errorBody);
        }

        return response;
    }


    public Set<Airline> getAllAirlines() throws IOException {
        RawHttpRequest request = http.parseRequest(
                "GET /airlines HTTP/1.1\r\nHost: " + host + "\r\nAccept: application/json\r\nConnection: keep-alive\r\n");
        RawHttpResponse<?> response = sendPersistentRequest(request);
        String json = response.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(json, new TypeToken<HashSet<AirlineImpl>>(){}.getType());
    }

    public Airline getAirlineById(int id) throws IOException {
        RawHttpRequest request = http.parseRequest(
                "GET /airlines/" + id + " HTTP/1.1\r\nHost: " + host + "\r\nAccept: application/json\r\nConnection: keep-alive\r\n");
        RawHttpResponse<?> response = sendPersistentRequest(request);
        String json = response.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(json, AirlineImpl.class);
    }

    public Airline createAirline(Airline airline) throws IOException {
        String json = gson.toJson(airline);
        return sendWithBody("POST", "/airlines", json);
    }

    public Airline updateAirline(Airline airline) throws IOException {
        String json = gson.toJson(airline);
        return sendWithBody("PUT", "/airlines/" + airline.getAirlineId(), json);
    }

    public void deleteAirline(int id) throws IOException {
        RawHttpRequest request = http.parseRequest(
                "DELETE /airlines/" + id + " HTTP/1.1\r\nHost: " + host + "\r\nConnection: keep-alive\r\n");
        sendPersistentRequest(request);
    }

    private Airline sendWithBody(String method, String path, String jsonBody) throws IOException {
        byte[] bytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        String hash = CryptoUtils.hash(bytes);

        RawHttpRequest request = http.parseRequest(
                        method + " " + path + " HTTP/1.1\r\n" +
                                "Host: " + host + "\r\n" +
                                "Content-Type: application/json; charset=utf-8\r\n" +
                                "Content-Length: " + bytes.length + "\r\n" +
                                "Accept: application/json\r\n" +
                                "Connection: keep-alive\r\n" +
                                HASH_HEADER + ": " + hash + "\r\n")
                .withBody(new StringBody(jsonBody, "application/json; charset=utf-8"));

        RawHttpResponse<?> response = sendPersistentRequest(request);
        String respJson = response.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(respJson, AirlineImpl.class);
    }
}