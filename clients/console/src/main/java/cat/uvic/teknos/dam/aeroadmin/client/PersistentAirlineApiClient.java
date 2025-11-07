package cat.uvic.teknos.dam.aeroadmin.client;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
// Imports per a la gestió d'inactivitat (Req 3)
import java.util.Timer;
import java.util.TimerTask;

/**
 * Un client API que manté una connexió persistent i gestiona la
 * inactivitat (Req 3).
 * Aquesta classe substitueix AirlineApiClient.
 */
public class PersistentAirlineApiClient {

    private final String host;
    private final int port;
    private Socket socket;
    private final RawHttp http;
    private final Gson gson;

    // REQ 3: Temporitzador d'inactivitat
    private Timer inactivityTimer;
    // 2 minuts en mil·lisegons
    private final long INACTIVITY_TIMEOUT_MS = 2 * 60 * 1000;

    public PersistentAirlineApiClient(String baseUrl) {
        URI uri = URI.create(baseUrl);
        this.host = uri.getHost();
        // Gestiona port per defecte si no s'especifica
        this.port = (uri.getPort() == -1) ? 80 : uri.getPort();
        this.http = new RawHttp();
        this.gson = new Gson();
    }

    /**
     * Obre la connexió persistent amb el servidor.
     */
    public void connect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            return; // Ja està connectat
        }
        this.socket = new Socket(host, port);
        System.out.println("Client connectat al servidor a " + host + ":" + port);
        // Iniciem el temporitzador d'inactivitat
        resetInactivityTimer();
    }

    /**
     * Envia el missatge de desconnexió (Req 3)
     */
    public void disconnect() throws IOException {
        if (socket == null || socket.isClosed()) {
            return;
        }

        System.out.println("Enviant sol·licitud de desconnexió...");
        // Aturem el temporitzador per evitar que torni a saltar
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
            inactivityTimer = null;
        }

        try {
            // 1. Enviar missatge "disconnect"
            RawHttpRequest request = http.parseRequest(
                    "GET /disconnect HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "Connection: keep-alive\r\n"
            );
            request.writeTo(socket.getOutputStream());

            // 2. Esperar ACK del servidor
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
            if (response.getStatusCode() == 200) {
                System.out.println("ACK rebut del servidor.");
            } else {
                System.err.println("Error rebent ACK: " + response.getStartLine());
            }

            // 3. Esperar 1 segon (Req 3)
            Thread.sleep(1000);

        } catch (Exception e) {
            // Si el servidor ja ha tancat el socket, pot donar error, ho ignorem
            if (!(e instanceof java.net.SocketException)) {
                System.err.println("Error durant la desconnexió: " + e.getMessage());
            }
        } finally {
            // 4. Tancar el socket
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Connexió tancada.");
        }
    }

    /**
     * Reinicia el comptador d'inactivitat. S'ha de cridar CADA VEGADA
     * que el client fa una acció (abans d'enviar una petició).
     */
    private void resetInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        inactivityTimer = new Timer(true); // true per a fil dimoni
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Si salta el timer, iniciem el procés de desconnexió
                System.out.println("\n[TIMER] 2 minuts d'inactivitat detectats.");
                try {
                    disconnect();
                } catch (IOException e) {
                    System.err.println("Error en desconnectar per inactivitat: " + e.getMessage());
                }
            }
        }, INACTIVITY_TIMEOUT_MS);
    }

    /**
     * Mètode privat per enviar peticions i rebre respostes
     * usant la connexió persistent.
     */
    private RawHttpResponse<?> sendPersistentRequest(RawHttpRequest request) throws IOException {
        if (socket == null || socket.isClosed()) {
            // Si el timer ha tancat el socket, intentem reconnectar
            System.out.println("Connexió tancada, intentant reconnectar...");
            connect();
            if (socket == null || socket.isClosed()) {
                throw new IOException("El client no està connectat. No s'ha pogut reconnectar.");
            }
        }

        // REQ 3: Cada petició reinicia el timer
        resetInactivityTimer();

        System.out.println("Enviant petició: " + request.getStartLine());
        request.writeTo(socket.getOutputStream());

        RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
        System.out.println("Resposta rebuda: " + response.getStartLine());

        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            String errorBody = response.getBody().map(b -> {
                try {
                    return b.decodeBodyToString(StandardCharsets.UTF_8);
                } catch (IOException e) { return "Could not decode error body."; }
            }).orElse("No error body.");
            throw new IOException("Error del servidor: " + response.getStatusCode() + " " + response.getStartLine().getReason() + " - " + errorBody);
        }
        return response;
    }

    // --- MÈTODES API (ARA USEN LA CONNEXIÓ PERSISTENT) ---
    // Afegeix "Connection: keep-alive" a totes les peticions

    public Set<Airline> getAllAirlines() throws IOException {
        RawHttpRequest request = http.parseRequest(
                "GET /airlines\r\n" +
                        "Host: " + host + "\r\n" +
                        "Accept: application/json\r\n" +
                        "Connection: keep-alive\r\n"
        );
        RawHttpResponse<?> response = sendPersistentRequest(request);
        String jsonBody = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        Type setType = new TypeToken<HashSet<AirlineImpl>>() {}.getType();
        return gson.fromJson(jsonBody, setType);
    }

    public Airline getAirlineById(int id) throws IOException {
        RawHttpRequest request = http.parseRequest(
                "GET /airlines/" + id + "\r\n" +
                        "Host: " + host + "\r\n" +
                        "Accept: application/json\r\n" +
                        "Connection: keep-alive\r\n"
        );
        RawHttpResponse<?> response = sendPersistentRequest(request);
        String jsonBody = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(jsonBody, AirlineImpl.class);
    }

    public Airline createAirline(Airline airline) throws IOException {
        String jsonBody = gson.toJson(airline);
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        StringBody body = new StringBody(jsonBody, "application/json; charset=utf-8");

        RawHttpRequest request = http.parseRequest(
                "POST /airlines\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Type: application/json; charset=utf-8\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "Accept: application/json\r\n" +
                        "Connection: keep-alive\r\n"
        ).withBody(body);

        RawHttpResponse<?> response = sendPersistentRequest(request);
        String responseJson = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(responseJson, AirlineImpl.class);
    }

    public Airline updateAirline(Airline airline) throws IOException {
        String jsonBody = gson.toJson(airline);
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        StringBody body = new StringBody(jsonBody, "application/json; charset=utf-8");

        RawHttpRequest request = http.parseRequest(
                "PUT /airlines/" + airline.getAirlineId() + "\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Type: application/json; charset=utf-8\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "Accept: application/json\r\n" +
                        "Connection: keep-alive\r\n"
        ).withBody(body);

        RawHttpResponse<?> response = sendPersistentRequest(request);
        String responseJson = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(responseJson, AirlineImpl.class);
    }

    public void deleteAirline(int id) throws IOException {
        RawHttpRequest request = http.parseRequest(
                "DELETE /airlines/" + id + "\r\n" +
                        "Host: " + host + "\r\n" +
                        "Connection: keep-alive\r\n"
        );
        sendPersistentRequest(request);
    }
}