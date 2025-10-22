package cat.uvic.teknos.dam.aeroadmin.model.model;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PilotAssignment {
    int getAssignmentId();
    void setAssignmentId(int assignmentId);

    Flight getFlight();
    void setFlight(Flight flight);

    Pilot getPilot();
    void setPilot(Pilot pilot);

    AssignmentRole getRole();
    void setRole(AssignmentRole role);

    boolean isLeadPilot();
    void setLeadPilot(boolean leadPilot);

    BigDecimal getAssignedHours();
    void setAssignedHours(BigDecimal assignedHours);

    // Propietat completada amb el seu getter i setter
    LocalDateTime getAssignedAt();
    void setAssignedAt(LocalDateTime assignedAt);
}