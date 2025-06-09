package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import java.util.Set;

public interface AircraftRepository {
    void save(Aircraft aircraft);
    void delete(Aircraft aircraft);
    Aircraft get(Integer id);
    Set<Aircraft> getAll();

    Set<Aircraft> getByManufacturer(String manufacturer);

    Aircraft create();
}