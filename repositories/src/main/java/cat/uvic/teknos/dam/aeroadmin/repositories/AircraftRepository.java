package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;

import java.util.Set;

public interface AircraftRepository extends Repository<Integer, Aircraft> {
    Set<Aircraft> getByManufacturer(String manufacturer);
}