package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pilot_assignment")
public class JpaPilotAssignment implements PilotAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int assignmentId;

    @ManyToOne
    @JoinColumn(name = "pilot_id", nullable = false)
    private JpaPilot pilot;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private JpaFlight flight;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    private String role; // e.g., "Captain", "First Officer"

    // Getters y setters

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    @Override
    public Pilot getPilot() {
        return pilot;
    }

    @Override
    public void setPilot(Pilot pilot) {
        this.pilot = (JpaPilot) pilot;
    }

    @Override
    public Flight getFlight() {
        return flight;
    }

    @Override
    public void setFlight(Flight flight) {
        this.flight = (JpaFlight) flight;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getRole() {
        return role;
    }

    @Override
    public void setRole(AssignmentRole role) {

    }

    @Override
    public boolean isLeadPilot() {
        return false;
    }

    @Override
    public void setLeadPilot(boolean leadPilot) {

    }

    @Override
    public BigDecimal getAssignedHours() {
        return null;
    }

    @Override
    public void setAssignedHours(BigDecimal assignedHours) {

    }

    public void setRole(String role) {
        this.role = role;
    }
}