package cat.uvic.teknos.dam.aeroadmin;

import cat.uvic.teknos.dam.aeroadmin.model.Aircraft;

import java.util.Set;

public interface AircraftRepository extends Repository<Integer, Aircraft> {
    Set<Aircraft> getByManufacturer(String manufacturer);
}