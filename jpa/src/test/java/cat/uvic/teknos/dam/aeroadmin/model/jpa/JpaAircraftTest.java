package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaAircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JpaAircraftTest {

    @Test
    void shouldSetAndGetProperties() {
        Aircraft aircraft = new JpaAircraft();

        aircraft.setAircraftId(1);
        aircraft.setModel("B737");
        aircraft.setManufacturer("Boeing");
        aircraft.setRegistrationNumber("EC-MAD");
        aircraft.setProductionYear(2020);

        assertEquals(1, aircraft.getAircraftId());
        assertEquals("B737", aircraft.getModel());
        assertEquals("Boeing", aircraft.getManufacturer());
        assertEquals("EC-MAD", aircraft.getRegistrationNumber());
        assertEquals(2020, aircraft.getProductionYear());
    }
}