package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaPilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JpaPilotTest {

    @Test
    void shouldSetAndGetProperties() {
        Pilot pilot = new JpaPilot();

        pilot.setPilotId(1);
        pilot.setFirstName("John");
        pilot.setLastName("Doe");
        pilot.setDateOfBirth(LocalDate.of(1985, 1, 1));
        pilot.setNationality("American");

        assertEquals(1, pilot.getPilotId());
        assertEquals("John", pilot.getFirstName());
        assertEquals("Doe", pilot.getLastName());
        assertEquals(LocalDate.of(1985, 1, 1), pilot.getDateOfBirth());
        assertEquals("American", pilot.getNationality());
    }
}