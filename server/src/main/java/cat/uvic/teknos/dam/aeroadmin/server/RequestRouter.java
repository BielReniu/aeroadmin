package cat.uvic.teknos.dam.aeroadmin.server;

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

    private final AirlineController airlineController;
    private final RawHttp http = new RawHttp();

    public RequestRouter(AirlineController airlineController) {
        this.airlineController = airlineController;
    }

    public RawHttpResponse<?> route(RawHttpRequest request) {
        String method = request.getMethod();
        String path = request.getUri().getPath();

        try {
            // Anàlisi bàsic de la ruta (exp: /airlines/1)
            String[] pathParts = path.split("/");
            String resource = pathParts.length > 1 ? pathParts[1] : "";
            String id = pathParts.length > 2 ? pathParts[2] : null;

            // Enrutament per al recurs "airlines"
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
                            String jsonBody = airlineController.createAirline(request);
                            return createJsonResponse(jsonBody, 201, "Created");
                        }
                        break;
                    case "PUT":
                        if (id != null) {
                            // PUT /airlines/{id}
                            String jsonBody = airlineController.updateAirline(id, request);
                            return createJsonResponse(jsonBody, 200, "OK");
                        }
                        break;
                    case "DELETE":
                        if (id != null) {
                            // DELETE /airlines/{id}
                            airlineController.deleteAirline(id);
                            // Un DELETE exitós retorna 204 No Content (sense body)
                            return createJsonResponse(null, 204, "No Content");
                        }
                        break;
                }
            }

            // Si no coincideix amb cap ruta, llencem un 404
            throw new NotFoundException("Resource not found for " + method + " " + path);

        } catch (HttpException e) {
            // Captura les nostres excepcions (400, 404, etc.)
            return createJsonResponse("{\"error\": \"" + e.getMessage() + "\"}", e.getStatusCode(), e.getStatusMessage());
        } catch (IOException e) {
            // Errors de lectura del body
            return createJsonResponse("{\"error\": \"Error reading request body: " + e.getMessage() + "\"}", 400, "Bad Request");
        } catch (Exception e) {
            // Captura qualsevol altre error (500)
            e.printStackTrace(); // Important per debugar
            return createJsonResponse("{\"error\": \"Internal Server Error: " + e.getMessage() + "\"}", 500, "Internal Server Error");
        }
    }

    private RawHttpResponse<?> createJsonResponse(String jsonBody, int statusCode, String statusMessage) {

        // Un 204 No Content NO HA de tenir body ni Content-Type
        if (statusCode == 204) {
            return http.parseResponse(
                    "HTTP/1.1 204 No Content\r\n" +
                            "Server: AeroAdmin-Server\r\n" +
                            "\r\n"
            );
        }

        // Assegurem que el body no sigui nul per als altres casos
        String body = (jsonBody == null) ? "{}" : jsonBody;
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        return http.parseResponse(
                        "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n" +
                                "Content-Type: application/json; charset=utf-8\r\n" +
                                "Content-Length: " + bodyBytes.length + "\r\n" +
                                "Server: AeroAdmin-Server\r\n" +
                                "\r\n"
                )
                .withBody(new StringBody(body, "application/json; charset=utf-8"));
    }
}