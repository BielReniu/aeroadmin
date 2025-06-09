package cat.uvic.teknos.dam.aeroadmin.model.impl;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PilotAssignmentImpl implements PilotAssignment {
    private int assignmentId;
    private Flight flight;
    private Pilot pilot;
    private AssignmentRole role;
    private boolean isLeadPilot;
    private BigDecimal assignedHours;

    @Override
    public int getAssignmentId() {
        return assignmentId;
    }

    @Override
    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    @Override
    public Flight getFlight() {
        return flight;
    }

    @Override
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    @Override
    public Pilot getPilot() {
        return pilot;
    }

    @Override
    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    @Override
    public AssignmentRole getRole() {
        return role;
    }

    @Override
    public void setRole(AssignmentRole role) {
        this.role = role;
    }

    @Override
    public boolean isLeadPilot() {
        return isLeadPilot;
    }

    @Override
    public void setLeadPilot(boolean leadPilot) {
        this.isLeadPilot = leadPilot;
    }

    @Override
    public BigDecimal getAssignedHours() {
        return assignedHours;
    }

    @Override
    public void setAssignedHours(BigDecimal assignedHours) {
        this.assignedHours = assignedHours;
    }

    @Override
    public void setAssignedAt(LocalDateTime parse) {

    }
}