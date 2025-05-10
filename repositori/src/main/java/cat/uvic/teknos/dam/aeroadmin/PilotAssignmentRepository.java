package cat.uvic.teknos.dam.aeroadmin;

import cat.uvic.teknos.dam.aeroadmin.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.PilotAssignment;

import java.util.Set;

public interface PilotAssignmentRepository extends Repository<Integer, PilotAssignment> {
    Set<PilotAssignment> getByFlight(Flight flight);
}