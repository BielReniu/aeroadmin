package cat.uvic.teknos.dam.aeroadmin.server;

import cat.uvic.teknos.dam.aeroadmin.server.controllers.SecurityController;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.CryptoUtils;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.EncryptionUtils;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final RequestRouter router;
    private final RawHttp rawHttp = new RawHttp();
    private final AtomicInteger connectedClients;

    private SecretKey sessionKey;

    public ClientHandler(Socket clientSocket, RequestRouter router, AtomicInteger connectedClients) {
        this.clientSocket = clientSocket;
        this.router = router;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run() {
        connectedClients.incrementAndGet();
        try {
            while (true) {
                RawHttpRequest request = rawHttp.parseRequest(clientSocket.getInputStream());

                if (request.getUri().getPath().equals("/disconnect")) {
                    System.out.println("Client " + clientSocket.getInetAddress() + " ha sol·licitat desconnexió.");
                    RawHttpResponse<?> ack = rawHttp.parseResponse("HTTP/1.1 200 OK\r\nServer: AeroAdmin-ACK\r\nConnection: close\r\n\r\n");
                    ack.writeTo(clientSocket.getOutputStream());
                    break;
                }

                if (request.getMethod().equals("GET") && request.getUri().getPath().startsWith("/keys/")) {
                    String clientId = request.getUri().getPath().substring("/keys/".length());
                    System.out.println("🔒 Iniciant handshake segur amb el client: " + clientId);

                    try {
                        SecurityController.HandshakeResult result = router.getSecurityController().generateKeyForClient(clientId);
                        this.sessionKey = result.serverKey;

                        String jsonResponse = "{\"key\": \"" + result.encryptedClientKey + "\"}";

                        RawHttpResponse<?> response = rawHttp.parseResponse("HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: application/json\r\n" +
                                        "Content-Length: " + jsonResponse.length() + "\r\n\r\n")
                                .withBody(new StringBody(jsonResponse));

                        response.writeTo(clientSocket.getOutputStream());
                        continue;

                    } catch (Exception e) {
                        System.err.println("❌ Error en el handshake: " + e.getMessage());
                        RawHttpResponse<?> errorRes = rawHttp.parseResponse("HTTP/1.1 403 Forbidden\r\n\r\n");
                        errorRes.writeTo(clientSocket.getOutputStream());
                        break;
                    }
                }

                if (sessionKey != null && request.getBody().isPresent()) {
                    try {
                        String encryptedBody = request.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
                        String decryptedBody = EncryptionUtils.decryptAES(encryptedBody, sessionKey);
                        String newHash = CryptoUtils.hash(decryptedBody);

                        RawHttpHeaders newHeaders = RawHttpHeaders.newBuilder()
                                .merge(request.getHeaders())
                                .overwrite("X-Message-Hash", newHash)
                                .build();

                        request = request.withBody(new StringBody(decryptedBody))
                                .withHeaders(newHeaders);

                    } catch (Exception e) {
                        System.err.println("❌ Error desencriptant petició: " + e.getMessage());
                        break;
                    }
                }

                RawHttpResponse<?> response = router.route(request);

                if (sessionKey != null && response.getBody().isPresent()) {
                    try {
                        String plainBody = response.getBody().get().decodeBodyToString(StandardCharsets.UTF_8);
                        String encryptedBody = EncryptionUtils.encryptAES(plainBody, sessionKey);

                        byte[] encryptedBytes = encryptedBody.getBytes(StandardCharsets.UTF_8);

                        RawHttpHeaders headers = RawHttpHeaders.newBuilder()
                                .merge(response.getHeaders())
                                .overwrite("Content-Length", String.valueOf(encryptedBytes.length))
                                .build();

                        response = response.withBody(new StringBody(encryptedBody))
                                .withHeaders(headers);

                    } catch (Exception e) {
                        System.err.println("❌ Error encriptant resposta: " + e.getMessage());
                    }
                }

                response.writeTo(clientSocket.getOutputStream());
            }

        } catch (IOException e) {
            System.err.println("Client desconnectat: " + clientSocket.getInetAddress());
        } finally {
            connectedClients.decrementAndGet();
            try {
                if (!clientSocket.isClosed()) clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error tancant socket: " + e.getMessage());
            }
        }
    }
}