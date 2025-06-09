package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;

import java.util.Set;

public interface PilotAssignmentRepository extends Repository<Integer, PilotAssignment> {
    Set<PilotAssignment> getByFlight(Flight flight);

    PilotAssignment create();
}