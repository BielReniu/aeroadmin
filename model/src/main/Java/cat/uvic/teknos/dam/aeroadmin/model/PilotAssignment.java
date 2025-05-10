package cat.uvic.teknos.dam.aeroadmin.model;

import cat.uvic.teknos.dam.aeroadmin.enums.AssignmentRole;

import java.math.BigDecimal;

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
}