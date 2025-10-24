package cat.uvic.teknos.dam.aeroadmin.server.controllers;

// Importa l'entitat JPA
import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaAirline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
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

    /** POST /airlines */
    public String createAirline(RawHttpRequest request) throws IOException {
        String jsonBody = request.getBody()
                .orElseThrow(() -> new BadRequestException("Request body is missing."))
                .decodeBodyToString(StandardCharsets.UTF_8);

        // --- CORRECCIÓ ---
        // Deserialitza directament a l'entitat JPA
        JpaAirline newAirline = gson.fromJson(jsonBody, JpaAirline.class);
        // --- FI CORRECCIÓ ---

        if (newAirline == null) {
            throw new BadRequestException("Invalid JSON format for Airline.");
        }

        // Assegura que l'ID sigui 0 per a una nova inserció
        newAirline.setAirlineId(0);

        repository.save(newAirline); // Ara passem el tipus correcte (JpaAirline)

        return gson.toJson(newAirline);
    }

    /** PUT /airlines/{id} */
    public String updateAirline(String id, RawHttpRequest request) throws IOException {
        int airlineId;
        try {
            airlineId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID format. Must be an integer.");
        }

        // Recuperem l'entitat existent (hauria de ser una instància de JpaAirline si el repo és JPA)
        Airline existingAirline = repository.get(airlineId);
        if (existingAirline == null) {
            throw new NotFoundException("Airline with ID " + airlineId + " not found.");
        }
        // Assegurem que treballem amb l'objecte JPA
        if (!(existingAirline instanceof JpaAirline)) {
            throw new RuntimeException("Repository did not return a JpaAirline instance for update."); // Error intern
        }
        JpaAirline existingJpaAirline = (JpaAirline) existingAirline;


        String jsonBody = request.getBody()
                .orElseThrow(() -> new BadRequestException("Request body is missing."))
                .decodeBodyToString(StandardCharsets.UTF_8);

        // --- CORRECCIÓ ---
        // Deserialitza la informació actualitzada a l'entitat JPA
        JpaAirline updatedInfo = gson.fromJson(jsonBody, JpaAirline.class);
        // --- FI CORRECCIÓ ---

        if (updatedInfo == null) {
            throw new BadRequestException("Invalid JSON format for Airline.");
        }

        // Actualitza l'objecte entitat existent
        existingJpaAirline.setAirlineName(updatedInfo.getAirlineName());
        existingJpaAirline.setIataCode(updatedInfo.getIataCode());
        existingJpaAirline.setIcaoCode(updatedInfo.getIcaoCode());
        existingJpaAirline.setCountry(updatedInfo.getCountry());
        existingJpaAirline.setFoundationYear(updatedInfo.getFoundationYear());
        existingJpaAirline.setWebsite(updatedInfo.getWebsite());

        repository.save(existingJpaAirline); // Passa l'entitat JpaAirline actualitzada
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
        // No cal retornar res (el router enviarà un 204 No Content)
    }
}