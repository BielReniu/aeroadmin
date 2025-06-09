package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.jpa.JpaAircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JpaAircraftDetailTest {

    @Test
    void shouldSetAndGetProperties() {
        AircraftDetail detail = new JpaAircraftDetail();

        detail.setAircraftId(1);
        detail.setPassengerCapacity(180);
        detail.setMaxRangeKm(6000);
        detail.setMaxSpeedKmh(900);
        detail.setFuelCapacityLiters(20000);

        assertEquals(1, detail.getAircraftId());
        assertEquals(180, detail.getPassengerCapacity());
        assertEquals(6000, detail.getMaxRangeKm());
        assertEquals(900, detail.getMaxSpeedKmh());
        assertEquals(20000, detail.getFuelCapacityLiters());
    }
}