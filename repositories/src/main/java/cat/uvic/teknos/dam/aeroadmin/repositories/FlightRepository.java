package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;

import java.util.Set;

public interface FlightRepository extends Repository<Integer, Flight> {
    Set<Flight> getByDepartureAirport(String departureAirport);
}