package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;

import java.util.Set;

public interface AircraftDetailRepository extends Repository<Integer, AircraftDetail> {
    Set<AircraftDetail> getByPassengerCapacity(int minCapacity, int maxCapacity);
}