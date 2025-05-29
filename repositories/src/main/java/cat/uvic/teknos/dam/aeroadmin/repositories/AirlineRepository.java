package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;

import java.util.Set;

public interface AirlineRepository extends Repository<Integer, Airline> {
    Set<Airline> getByCountry(String country);
}