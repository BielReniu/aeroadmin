package cat.uvic.teknos.dam.aeroadmin;

import cat.uvic.teknos.dam.aeroadmin.model.Flight;

import java.util.Set;

public interface FlightRepository extends Repository<Integer, Flight> {
    Set<Flight> getByDepartureAirport(String departureAirport);
}