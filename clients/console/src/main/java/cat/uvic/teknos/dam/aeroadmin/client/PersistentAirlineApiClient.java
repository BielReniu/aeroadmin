package cat.uvic.teknos.dam.aeroadmin.client;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.CryptoUtils;
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
 * An API client that maintains a persistent connection and handles
 * inactivity.
 * This class replaces AirlineApiClient.
 */
public class PersistentAirlineApiClient {

    private final String host;
    private final int port;
    private Socket socket;
    private final RawHttp http;
    private final Gson gson;

    // Custom header for message hash
    private static final String HASH_HEADER = "X-Message-Hash";

    // Inactivity timer
    private Timer inactivityTimer;
    // 2 minutes in milliseconds
    private final long INACTIVITY_TIMEOUT_MS = 2 * 60 * 1000;

    public PersistentAirlineApiClient(String baseUrl) {
        URI uri = URI.create(baseUrl);
        this.host = uri.getHost();
        // Handle default port if not specified
        this.port = (uri.getPort() == -1) ? 80 : uri.getPort();
        this.http = new RawHttp();
        this.gson = new Gson();
    }

    /**
     * Opens the persistent connection to the server.
     */
    public void connect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            return; // Already connected
        }
        this.socket = new Socket(host, port);
        System.out.println("Client connected to server at " + host + ":" + port);
        // Start the inactivity timer
        resetInactivityTimer();
    }

    /**
     * Sends the disconnect message.
     */
    public void disconnect() throws IOException {
        if (socket == null || socket.isClosed()) {
            return;
        }

        System.out.println("Sending disconnect request...");
        // Stop the timer to prevent it from firing again
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
            inactivityTimer = null;
        }

        try {
            // 1. Send "disconnect" message
            RawHttpRequest request = http.parseRequest(
                    "GET /disconnect HTTP/1.1\r\n" +
                            "Host: " + host + "\r\n" +
                            "Connection: keep-alive\r\n"
            );
            request.writeTo(socket.getOutputStream());

            // 2. Wait for server ACK
            RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
            if (response.getStatusCode() == 200) {
                System.out.println("ACK received from server.");
            } else {
                System.err.println("Error receiving ACK: " + response.getStartLine());
            }

            // 3. Wait 1 second
            Thread.sleep(1000);

        } catch (Exception e) {
            // If server already closed the socket, this might fail, ignore
            if (!(e instanceof java.net.SocketException)) {
                System.err.println("Error during disconnect: " + e.getMessage());
            }
        } finally {
            // 4. Close the socket
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Connection closed.");
        }
    }

    /**
     * Resets the inactivity timer. Must be called EVERY TIME
     * the client performs an action (before sending a request).
     */
    private void resetInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        inactivityTimer = new Timer(true); // true for daemon thread
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If the timer fires, start the disconnect process
                System.out.println("\n[TIMER] 2 minutes of inactivity detected.");
                try {
                    disconnect();
                } catch (IOException e) {
                    System.err.println("Error disconnecting due to inactivity: " + e.getMessage());
                }
            }
        }, INACTIVITY_TIMEOUT_MS);
    }

    /**
     * Private method to send requests and receive responses
     * using the persistent connection.
     * <p>
     * Modified: Validates the hash of incoming responses.
     */
    private RawHttpResponse<?> sendPersistentRequest(RawHttpRequest request) throws IOException {
        if (socket == null || socket.isClosed()) {
            // If the timer closed the socket, try to reconnect
            System.out.println("Connection closed, attempting reconnect...");
            connect();
            if (socket == null || socket.isClosed()) {
                throw new IOException("Client is not connected. Failed to reconnect.");
            }
        }

        // Each request resets the timer
        resetInactivityTimer();

        System.out.println("Sending request: " + request.getStartLine());
        request.writeTo(socket.getOutputStream());

        // Read the response and load it into memory (eagerly)
        RawHttpResponse<?> response = http.parseResponse(socket.getInputStream()).eagerly();
        System.out.println("Response received: " + response.getStartLine());

        // --- HASH VALIDATION ---
        var bodyOpt = response.getBody();
        if (bodyOpt.isPresent()) {
            // If body is present, hash MUST be present
            String expectedHash = response.getHeaders().getFirst(HASH_HEADER)
                    .orElseThrow(() -> new IOException("Server response is missing " + HASH_HEADER + " header."));

            // The correct method to eagerly read bytes from a BodyReader is decodeBody()
            byte[] bodyBytes = bodyOpt.get().decodeBody();

            String actualHash = CryptoUtils.hash(bodyBytes);

            if (!actualHash.equals(expectedHash)) {
                throw new IOException("Response hash mismatch. Data integrity compromised.");
            }
            System.out.println("Response hash verification: OK");
        }
        // --- END HASH VALIDATION ---


        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            String errorBody = response.getBody().map(b -> {
                try {
                    return b.decodeBodyToString(StandardCharsets.UTF_8);
                } catch (IOException e) { return "Could not decode error body."; }
            }).orElse("No error body.");
            throw new IOException("Server error: " + response.getStatusCode() + " " + response.getStartLine().getReason() + " - " + errorBody);
        }
        return response;
    }

    // --- API METHODS (NOW USING PERSISTENT CONNECTION) ---
    // Added "Connection: keep-alive" to all requests

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

    /**
     * Modified: Adds X-Message-Hash header
     */
    public Airline createAirline(Airline airline) throws IOException {
        String jsonBody = gson.toJson(airline);
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        StringBody body = new StringBody(jsonBody, "application/json; charset=utf-8");

        // --- ADDED ---
        String hash = CryptoUtils.hash(bodyBytes);
        // --- END ADDED ---

        RawHttpRequest request = http.parseRequest(
                "POST /airlines\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Type: application/json; charset=utf-8\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "Accept: application/json\r\n" +
                        "Connection: keep-alive\r\n" +
                        HASH_HEADER + ": " + hash + "\r\n" // <-- AQUESTA ÉS LA LÍNIA CORRECTA
        ).withBody(body);

        RawHttpResponse<?> response = sendPersistentRequest(request);
        String responseJson = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(responseJson, AirlineImpl.class);
    }

    /**
     * Modified: Adds X-Message-Hash header
     */
    public Airline updateAirline(Airline airline) throws IOException {
        String jsonBody = gson.toJson(airline);
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        StringBody body = new StringBody(jsonBody, "application/json; charset=utf-8");

        // --- ADDED ---
        String hash = CryptoUtils.hash(bodyBytes);
        // --- END ADDED ---

        RawHttpRequest request = http.parseRequest(
                "PUT /airlines/" + airline.getAirlineId() + "\r\n" +
                        "Host: " + host + "\r\n" +
                        "Content-Type: application/json; charset=utf-8\r\n" +
                        "Content-Length: " + bodyBytes.length + "\r\n" +
                        "Accept: application/json\r\n" +
                        "Connection: keep-alive\r\n" +
                        HASH_HEADER + ": " + hash + "\r\n" // <-- AQUESTA ÉS LA LÍNIA CORRECTA
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