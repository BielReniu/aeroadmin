package cat.uvic.teknos.dam.aeroadmin.server;

import cat.uvic.teknos.dam.aeroadmin.server.controllers.AirlineController;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.nio.charset.StandardCharsets;

public class RequestRouter {

    private final AirlineController airlineController;
    private final RawHttp http = new RawHttp();

    public RequestRouter(AirlineController airlineController) {
        this.airlineController = airlineController;
    }

    public RawHttpResponse<?> route(RawHttpRequest request) {
        var method = request.getMethod();
        var path = request.getUri().getPath();

        if (method.equals("GET") && path.equals("/airlines")) {
            String jsonBody = airlineController.getAllAirlines();
            return createJsonResponse(jsonBody, 200, "OK");
        }

        return createJsonResponse("{\"error\": \"Resource not found\"}", 404, "Not Found");
    }

    private RawHttpResponse<?> createJsonResponse(String jsonBody, int statusCode, String statusMessage) {
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);

        return http.parseResponse(
                        "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n" +
                                "Content-Type: application/json; charset=utf-8\r\n" +
                                "Content-Length: " + bodyBytes.length + "\r\n" +
                                "Server: AeroAdmin-Server\r\n" +
                                "\r\n"
                )
                // CORRECCIÓ AQUÍ: Passem el String original, no l'array de bytes.
                .withBody(new StringBody(jsonBody, "application/json"));
    }
}