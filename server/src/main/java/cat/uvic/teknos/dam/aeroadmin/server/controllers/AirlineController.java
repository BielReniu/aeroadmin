package cat.uvic.teknos.dam.aeroadmin.server.controllers;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl; // Important: utilitza la implementació
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
        this.gson = new Gson(); // Instanciem Gson aquí
    }

    /**
     * GET /airlines
     * Obté totes les aerolínies i les retorna com una cadena de text JSON.
     */
    public String getAllAirlines() {
        Set<Airline> airlines = repository.getAll();
        return gson.toJson(airlines);
    }

    /**
     * GET /airlines/{id}
     * Obté una aerolínia específica pel seu ID.
     */
    public String getAirlineById(String id) {
        try {
            int airlineId = Integer.parseInt(id);
            Airline airline = repository.get(airlineId);
            if (airline == null) {
                // Si no es troba, llencem una excepció 404
                throw new NotFoundException("Airline with ID " + airlineId + " not found.");
            }
            return gson.toJson(airline);
        } catch (NumberFormatException e) {
            // Si l'ID no és un número, llencem una excepció 400
            throw new BadRequestException("Invalid ID format. Must be an integer.");
        }
    }

    /**
     * POST /airlines
     * Crea una nova aerolínia a partir del cos de la petició (JSON).
     */
    public String createAirline(RawHttpRequest request) throws IOException {
        // Obtenim el cos de la petició (el JSON)
        String jsonBody = request.getBody()
                .orElseThrow(() -> new BadRequestException("Request body is missing."))
                .decodeBodyToString(StandardCharsets.UTF_8);

        // Usem Gson per convertir el JSON a un objecte Airline
        // Important utilitzar AirlineImpl.class per a la deserialització
        Airline newAirline = gson.fromJson(jsonBody, AirlineImpl.class);
        if (newAirline == null) {
            throw new BadRequestException("Invalid JSON format for Airline.");
        }

        // Assegurem que l'ID sigui 0 per a una nova inserció
        newAirline.setAirlineId(0);

        repository.save(newAirline);
        // Retornem l'objecte creat (que ara ja inclou el nou ID)
        return gson.toJson(newAirline);
    }

    /**
     * PUT /airlines/{id}
     * Actualitza una aerolínia existent.
     */
    public String updateAirline(String id, RawHttpRequest request) throws IOException {
        int airlineId;
        try {
            airlineId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID format. Must be an integer.");
        }

        // Comprovem si l'aerolínia que es vol actualitzar existeix
        Airline existingAirline = repository.get(airlineId);
        if (existingAirline == null) {
            throw new NotFoundException("Airline with ID " + airlineId + " not found.");
        }

        // Obtenim el JSON amb les noves dades
        String jsonBody = request.getBody()
                .orElseThrow(() -> new BadRequestException("Request body is missing."))
                .decodeBodyToString(StandardCharsets.UTF_8);

        Airline updatedInfo = gson.fromJson(jsonBody, AirlineImpl.class);
        if (updatedInfo == null) {
            throw new BadRequestException("Invalid JSON format for Airline.");
        }

        // Actualitzem les dades de l'objecte que ja teníem
        existingAirline.setAirlineName(updatedInfo.getAirlineName());
        existingAirline.setIataCode(updatedInfo.getIataCode());
        existingAirline.setIcaoCode(updatedInfo.getIcaoCode());
        existingAirline.setCountry(updatedInfo.getCountry());
        existingAirline.setFoundationYear(updatedInfo.getFoundationYear());
        existingAirline.setWebsite(updatedInfo.getWebsite());

        // Desem els canvis
        repository.save(existingAirline);
        return gson.toJson(existingAirline);
    }

    /**
     * DELETE /airlines/{id}
     * Esborra una aerolínia pel seu ID.
     */
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

        // Esborrem l'aerolínia
        repository.delete(airline);
        // No cal retornar res (el router enviarà un 204 No Content)
    }
}