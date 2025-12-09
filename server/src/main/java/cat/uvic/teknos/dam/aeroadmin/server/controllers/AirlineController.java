package cat.uvic.teknos.dam.aeroadmin.server.controllers;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaAirline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.utilities.security.CryptoUtils;
import cat.uvic.teknos.dam.aeroadmin.server.exceptions.BadRequestException;
import cat.uvic.teknos.dam.aeroadmin.server.exceptions.NotFoundException;
import com.google.gson.Gson;
import rawhttp.core.RawHttpRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class AirlineController {

    private final AirlineRepository repository;
    private final Gson gson;

    private static final String HASH_HEADER = "X-Message-Hash";

    public AirlineController(AirlineRepository repository) {
        this.repository = repository;
        this.gson = new Gson();
    }

    /** GET /airlines */
    public String getAllAirlines() {
        Set<Airline> airlines = repository.getAll();
        return gson.toJson(airlines);
    }

    /** GET /airlines/{id} */
    public String getAirlineById(String id) {
        try {
            int airlineId = Integer.parseInt(id);
            Airline airline = repository.get(airlineId);
            if (airline == null) {
                throw new NotFoundException("Airline with ID " + airlineId + " not found.");
            }
            return gson.toJson(airline);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID format. Must be an integer.");
        }
    }

    /**
     * Helper method to validate the hash of an incoming request.
     *
     * @param request The incoming request.
     * @return The raw byte[] of the body if the hash is valid.
     * @throws IOException         if the body cannot be read.
     * @throws BadRequestException if the hash header is missing or the hash does not match.
     */
    private byte[] validateAndGetBody(RawHttpRequest request) throws IOException {
        String expectedHash = request.getHeaders().getFirst(HASH_HEADER)
                .orElseThrow(() -> new BadRequestException("Missing " + HASH_HEADER + " header."));

        byte[] bodyBytes = request.getBody()
                .orElseThrow(() -> new BadRequestException("Request body is missing."))
                .decodeBody();

        String actualHash = CryptoUtils.hash(bodyBytes);

        if (!actualHash.equals(expectedHash)) {
            throw new BadRequestException("Hash mismatch. Message integrity compromised.");
        }

        return bodyBytes;
    }

    /**
     * POST /airlines
     * Validates hash before processing.
     */
    public String createAirline(RawHttpRequest request) throws IOException {
        byte[] bodyBytes = validateAndGetBody(request);
        String jsonBody = new String(bodyBytes, StandardCharsets.UTF_8);

        JpaAirline newAirline = gson.fromJson(jsonBody, JpaAirline.class);

        if (newAirline == null) {
            throw new BadRequestException("Invalid JSON format for Airline.");
        }

        newAirline.setAirlineId(0);

        repository.save(newAirline);

        return gson.toJson(newAirline);
    }

    /**
     * PUT /airlines/{id}
     * Validates hash before processing.
     */
    public String updateAirline(String id, RawHttpRequest request) throws IOException {
        int airlineId;
        try {
            airlineId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID format. Must be an integer.");
        }

        Airline existingAirline = repository.get(airlineId);
        if (existingAirline == null) {
            throw new NotFoundException("Airline with ID " + airlineId + " not found.");
        }
        if (!(existingAirline instanceof JpaAirline)) {
            throw new RuntimeException("Repository did not return a JpaAirline instance for update.");
        }
        JpaAirline existingJpaAirline = (JpaAirline) existingAirline;

        byte[] bodyBytes = validateAndGetBody(request);
        String jsonBody = new String(bodyBytes, StandardCharsets.UTF_8);

        JpaAirline updatedInfo = gson.fromJson(jsonBody, JpaAirline.class);

        if (updatedInfo == null) {
            throw new BadRequestException("Invalid JSON format for Airline.");
        }

        existingJpaAirline.setAirlineName(updatedInfo.getAirlineName());
        existingJpaAirline.setIataCode(updatedInfo.getIataCode());
        existingJpaAirline.setIcaoCode(updatedInfo.getIcaoCode());
        existingJpaAirline.setCountry(updatedInfo.getCountry());
        existingJpaAirline.setFoundationYear(updatedInfo.getFoundationYear());
        existingJpaAirline.setWebsite(updatedInfo.getWebsite());

        repository.save(existingJpaAirline);
        return gson.toJson(existingJpaAirline);
    }

    /** DELETE /airlines/{id} */
    public void deleteAirline(String id) {
        int airlineId;
        try {
            airlineId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID format. Must be an integer.");
        }

        Airline airline = repository.get(airlineId);
        if (airline == null) {
            throw new NotFoundException("Airline with ID " + airlineId + " not found.");
        }

        repository.delete(airline);
    }
}