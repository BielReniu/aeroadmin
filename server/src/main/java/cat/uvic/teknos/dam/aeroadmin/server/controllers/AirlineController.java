package cat.uvic.teknos.dam.aeroadmin.server.controllers;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import com.google.gson.Gson; // Importem Gson

import java.util.Set;

public class AirlineController {

    private final AirlineRepository repository;
    private final Gson gson;

    public AirlineController(AirlineRepository repository) {
        this.repository = repository;
        this.gson = new Gson();
    }

    /**
     * Obté totes les aerolínies i les retorna com una cadena de text JSON.
     */
    public String getAllAirlines() {
        Set<Airline> airlines = repository.getAll();
        return gson.toJson(airlines);
    }

    // Aquí aniran els altres mètodes: getAirlineById, createAirline, etc.
}