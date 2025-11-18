package cat.uvic.teknos.dam.aeroadmin.server;

import cat.uvic.teknos.dam.aeroadmin.utilities.security.CryptoUtils;
import cat.uvic.teknos.dam.aeroadmin.server.controllers.AirlineController;
import cat.uvic.teknos.dam.aeroadmin.server.exceptions.HttpException;
import cat.uvic.teknos.dam.aeroadmin.server.exceptions.NotFoundException;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestRouter {

    // Custom header for message hash
    private static final String HASH_HEADER = "X-Message-Hash";

    private final AirlineController airlineController;
    private final RawHttp http = new RawHttp();

    public RequestRouter(AirlineController airlineController) {
        this.airlineController = airlineController;
    }

    public RawHttpResponse<?> route(RawHttpRequest request) {
        String method = request.getMethod();
        String path = request.getUri().getPath();

        try {
            // Basic path parsing (e.g., /airlines/1)
            String[] pathParts = path.split("/");
            String resource = pathParts.length > 1 ? pathParts[1] : "";
            String id = pathParts.length > 2 ? pathParts[2] : null;

            // Routing for "airlines" resource
            if (resource.equals("airlines")) {
                switch (method) {
                    case "GET":
                        if (id != null) {
                            // GET /airlines/{id}
                            String jsonBody = airlineController.getAirlineById(id);
                            return createJsonResponse(jsonBody, 200, "OK");
                        } else {
                            // GET /airlines
                            String jsonBody = airlineController.getAllAirlines();
                            return createJsonResponse(jsonBody, 200, "OK");
                        }
                    case "POST":
                        if (id == null) {
                            // POST /airlines
                            // Hash validation is done inside the controller
                            String jsonBody = airlineController.createAirline(request);
                            return createJsonResponse(jsonBody, 201, "Created");
                        }
                        break;
                    case "PUT":
                        if (id != null) {
                            // PUT /airlines/{id}
                            // Hash validation is done inside the controller
                            String jsonBody = airlineController.updateAirline(id, request);
                            return createJsonResponse(jsonBody, 200, "OK");
                        }
                        break;
                    case "DELETE":
                        if (id != null) {
                            // DELETE /airlines/{id}
                            airlineController.deleteAirline(id);
                            // A successful DELETE returns 204 No Content (no body)
                            return createJsonResponse(null, 204, "No Content");
                        }
                        break;
                }
            }

            // If no route matches, throw 404
            throw new NotFoundException("Resource not found for " + method + " " + path);

        } catch (HttpException e) {
            // Catch our custom exceptions (400, 404, etc.)
            return createJsonResponse("{\"error\": \"" + e.getMessage() + "\"}", e.getStatusCode(), e.getStatusMessage());
        } catch (IOException e) {
            // Body reading errors
            return createJsonResponse("{\"error\": \"Error reading request body: " + e.getMessage() + "\"}", 400, "Bad Request");
        } catch (Exception e) {
            // Catch any other error (500)
            e.printStackTrace(); // Important for debugging
            return createJsonResponse("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}", 500, "Internal Server Error");
        }
    }

    /**
     * Creates a JSON response, adding the X-Message-Hash header if a body is present.
     */
    private RawHttpResponse<?> createJsonResponse(String jsonBody, int statusCode, String statusMessage) {

        // 204 No Content MUST NOT have a body or Content-Type
        if (statusCode == 204) {
            return http.parseResponse(
                    "HTTP/1.1 204 No Content\r\n" +
                            "Server: AeroAdmin-Server\r\n" +
                            "Connection: keep-alive\r\n" + // Keep connection alive
                            "\r\n"
            );
        }

        // Ensure body is not null for other cases
        String body = (jsonBody == null) ? "{}" : jsonBody;
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        // --- ADDED ---
        // Calculate hash of the response body
        String hash = CryptoUtils.hash(bodyBytes);
        // --- END ADDED ---

        return http.parseResponse(
                        "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n" +
                                "Content-Type: application/json; charset=utf-8\r\n" +
                                "Content-Length: " + bodyBytes.length + "\r\n" +
                                "Server: AeroAdmin-Server\r\n" +
                                "Connection: keep-alive\r\n" + // Keep connection alive
                                HASH_HEADER + ": " + hash + "\r\n" + // ADDED: Hash header
                                "\r\n"
                )
                .withBody(new StringBody(body, "application/json; charset=utf-8"));
    }
}