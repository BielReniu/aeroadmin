package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.model;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import java.math.BigDecimal;

public class JdbcPilotAssignment {

    private int assignmentId;
    private JdbcFlight flight;
    private JdbcPilot pilot;
    private AssignmentRole role;
    private boolean leadPilot;
    private BigDecimal assignedHours;

    // Getters i Setters
    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public JdbcFlight getFlight() {
        return flight;
    }

    public void setFlight(JdbcFlight flight) {
        this.flight = flight;
    }

    public JdbcPilot getPilot() {
        return pilot;
    }

    public void setPilot(JdbcPilot pilot) {
        this.pilot = pilot;
    }

    public AssignmentRole getRole() {
        return role;
    }

    public void setRole(AssignmentRole role) {
        this.role = role;
    }

    public boolean isLeadPilot() {
        return leadPilot;
    }

    public void setLeadPilot(boolean leadPilot) {
        this.leadPilot = leadPilot;
    }

    public BigDecimal getAssignedHours() {
        return assignedHours;
    }

    public void setAssignedHours(BigDecimal assignedHours) {
        this.assignedHours = assignedHours;
    }
}