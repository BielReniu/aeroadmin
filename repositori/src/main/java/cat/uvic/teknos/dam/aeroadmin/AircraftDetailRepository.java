package cat.uvic.teknos.dam.aeroadmin;

import cat.uvic.teknos.dam.aeroadmin.model.AircraftDetail;  // <--- IMPORT CORRECTE
import java.util.Set;

public interface AircraftDetailRepository extends Repository<Integer, AircraftDetail> {
    Set<AircraftDetail> getByPassengerCapacity(int minCapacity, int maxCapacity);
}