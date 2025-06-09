package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.jpa.JpaPilotAssignment;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JpaPilotAssignmentTest {

    @Test
    void shouldSetAndGetProperties() {
        PilotAssignment assignment = new JpaPilotAssignment();

        assignment.setAssignmentId(1);
        assignment.setRole(AssignmentRole.valueOf("Captain"));
        ((JpaPilotAssignment) assignment).setAssignedAt(LocalDateTime.now());

        assertEquals(1, assignment.getAssignmentId());
        assertEquals("Captain", assignment.getRole());
        assertNotNull(((JpaPilotAssignment) assignment).getAssignedAt());
    }
}