package cat.uvic.teknos.dam.aeroadmin.server;

import cat.uvic.teknos.dam.aeroadmin.server.controllers.AirlineController;
import cat.uvic.teknos.dam.aeroadmin.server.controllers.SecurityController;
import cat.uvic.teknos.dam.aeroadmin.server.exceptions.HttpException;
import cat.uvic.teknos.dam.aeroadmin.server.exceptions.NotFoundException;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.CryptoUtils;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RequestRouter {

    private static final String HASH_HEADER = "X-Message-Hash";

    private final AirlineController airlineController;
    private final SecurityController securityController;
    private final RawHttp http = new RawHttp();

    public RequestRouter(AirlineController airlineController, SecurityController securityController) {
        this.airlineController = airlineController;
        this.securityController = securityController;
    }

    public SecurityController getSecurityController() {
        return securityController;
    }

    public RawHttpResponse<?> route(RawHttpRequest request) {
        String method = request.getMethod();
        String path = request.getUri().getPath();

        try {
            String[] pathParts = path.split("/");
            String resource = pathParts.length > 1 ? pathParts[1] : "";
            String id = pathParts.length > 2 ? pathParts[2] : null;

            if (resource.equals("airlines")) {
                switch (method) {
                    case "GET":
                        if (id != null) {
                            return createJsonResponse(airlineController.getAirlineById(id), 200, "OK");
                        } else {
                            return createJsonResponse(airlineController.getAllAirlines(), 200, "OK");
                        }
                    case "POST":
                        if (id == null) {
                            return createJsonResponse(airlineController.createAirline(request), 201, "Created");
                        }
                        break;
                    case "PUT":
                        if (id != null) {
                            return createJsonResponse(airlineController.updateAirline(id, request), 200, "OK");
                        }
                        break;
                    case "DELETE":
                        if (id != null) {
                            airlineController.deleteAirline(id);
                            return createJsonResponse(null, 204, "No Content");
                        }
                        break;
                }
            }

            throw new NotFoundException("Resource not found for " + method + " " + path);

        } catch (HttpException e) {
            return createJsonResponse("{\"error\": \"" + e.getMessage() + "\"}", e.getStatusCode(), e.getStatusMessage());
        } catch (IOException e) {
            return createJsonResponse("{\"error\": \"Error reading request body: " + e.getMessage() + "\"}", 400, "Bad Request");
        } catch (Exception e) {
            e.printStackTrace();
            return createJsonResponse("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}", 500, "Internal Server Error");
        }
    }

    private RawHttpResponse<?> createJsonResponse(String jsonBody, int statusCode, String statusMessage) {
        if (statusCode == 204) {
            return http.parseResponse("HTTP/1.1 204 No Content\r\nServer: AeroAdmin-Server\r\nConnection: keep-alive\r\n\r\n");
        }

        String body = (jsonBody == null) ? "{}" : jsonBody;
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        String hash = CryptoUtils.hash(bodyBytes);

        return http.parseResponse(
                        "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n" +
                                "Content-Type: application/json; charset=utf-8\r\n" +
                                "Content-Length: " + bodyBytes.length + "\r\n" +
                                "Server: AeroAdmin-Server\r\n" +
                                "Connection: keep-alive\r\n" +
                                HASH_HEADER + ": " + hash + "\r\n" +
                                "\r\n"
                )
                .withBody(new StringBody(body, "application/json; charset=utf-8"));
    }
}