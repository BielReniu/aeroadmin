package cat.uvic.teknos.dam.aeroadmin.client;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;
import rawhttp.core.client.TcpRawHttpClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Aquesta classe s'encarrega exclusivament de la comunicació HTTP amb el servidor
 * per a la gestió d'Aerolínies.
 */
public class AirlineApiClient {

    private final String baseUrl;
    private final TcpRawHttpClient client;
    private final RawHttp http;
    private final Gson gson;

    public AirlineApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = new TcpRawHttpClient();
        this.http = new RawHttp();
        this.gson = new Gson();
    }

    /**
     * Mètode privat per enviar la petició i gestionar la resposta
     */
    private RawHttpResponse<?> sendRequest(RawHttpRequest request) throws IOException {
        System.out.println("Enviant petició: " + request.getStartLine());
        RawHttpResponse<?> response = client.send(request).eagerly();
        System.out.println("Resposta rebuda: " + response.getStartLine());

        // Gestió d'errors
        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            String errorBody = response.getBody().map(b -> {
                try {
                    return b.decodeBodyToString(StandardCharsets.UTF_8);
                } catch (IOException e) { return "Could not decode error body."; }
            }).orElse("No error body.");

            // Llencem una excepció perquè el Manager la capturi
            throw new IOException("Error del servidor: " + response.getStatusCode() + " " + response.getStartLine().getReason() + " - " + errorBody);
        }
        return response;
    }

    // GET /airlines
    public Set<Airline> getAllAirlines() throws IOException {
        RawHttpRequest request = http.parseRequest(
                "GET " + baseUrl + "/airlines\r\n" +
                        "Host: localhost\r\n" +
                        "Accept: application/json\r\n"
        );

        RawHttpResponse<?> response = sendRequest(request);
        String jsonBody = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);

        // Usem TypeToken per deserialitzar una llista/set
        Type setType = new TypeToken<HashSet<AirlineImpl>>() {}.getType();
        return gson.fromJson(jsonBody, setType);
    }

    // GET /airlines/{id}
    public Airline getAirlineById(int id) throws IOException {
        RawHttpRequest request = http.parseRequest(
                "GET " + baseUrl + "/airlines/" + id + "\r\n" +
                        "Host: localhost\r\n" +
                        "Accept: application/json\r\n"
        );

        RawHttpResponse<?> response = sendRequest(request);
        String jsonBody = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(jsonBody, AirlineImpl.class);
    }

    // POST /airlines
    public Airline createAirline(Airline airline) throws IOException {
        String jsonBody = gson.toJson(airline);
        // Calcula els bytes i la longitud ABANS
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        long contentLength = bodyBytes.length;

        // Crea el StringBody (ara només per passar-lo al withBody)
        StringBody body = new StringBody(jsonBody, "application/json; charset=utf-8");

        RawHttpRequest request = http.parseRequest(
                "POST " + baseUrl + "/airlines\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Type: application/json; charset=utf-8\r\n" +
                        // Utilitza la longitud calculada
                        "Content-Length: " + contentLength + "\r\n" +
                        "Accept: application/json\r\n"
        ).withBody(body);

        RawHttpResponse<?> response = sendRequest(request);
        String responseJson = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(responseJson, AirlineImpl.class);
    }

    // PUT /airlines/{id}
    public Airline updateAirline(Airline airline) throws IOException {
        String jsonBody = gson.toJson(airline);
        // Calcula els bytes i la longitud ABANS
        byte[] bodyBytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        long contentLength = bodyBytes.length;

        // Crea el StringBody
        StringBody body = new StringBody(jsonBody, "application/json; charset=utf-8");

        RawHttpRequest request = http.parseRequest(
                "PUT " + baseUrl + "/airlines/" + airline.getAirlineId() + "\r\n" +
                        "Host: localhost\r\n" +
                        "Content-Type: application/json; charset=utf-8\r\n" +
                        // Utilitza la longitud calculada
                        "Content-Length: " + contentLength + "\r\n" +
                        "Accept: application/json\r\n"
        ).withBody(body);

        RawHttpResponse<?> response = sendRequest(request);
        String responseJson = response.getBody().orElseThrow().decodeBodyToString(StandardCharsets.UTF_8);
        return gson.fromJson(responseJson, AirlineImpl.class);
    }

    // DELETE /airlines/{id}
    public void deleteAirline(int id) throws IOException {
        RawHttpRequest request = http.parseRequest(
                "DELETE " + baseUrl + "/airlines/" + id + "\r\n" +
                        "Host: localhost\r\n"
        );

        // Només enviem la petició. Si no llença excepció, tot ha anat bé (204)
        sendRequest(request);
    }
}