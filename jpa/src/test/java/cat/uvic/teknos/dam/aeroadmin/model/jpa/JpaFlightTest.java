package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.jpa.JpaFlight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JpaFlightTest {

    @Test
    void shouldSetAndGetProperties() {
        Flight flight = new JpaFlight();

        flight.setFlightId(1);
        flight.setFlightNumber("IB3201");
        flight.setDepartureAirport("MAD");
        flight.setArrivalAirport("BCN");

        LocalDateTime scheduledDeparture = LocalDateTime.of(2023, 12, 1, 10, 0);
        LocalDateTime scheduledArrival = LocalDateTime.of(2023, 12, 1, 11, 30);

        flight.setScheduledDeparture(scheduledDeparture);
        flight.setScheduledArrival(scheduledArrival);

        assertEquals(1, flight.getFlightId());
        assertEquals("IB3201", flight.getFlightNumber());
        assertEquals("MAD", flight.getDepartureAirport());
        assertEquals("BCN", flight.getArrivalAirport());
        assertEquals(scheduledDeparture, flight.getScheduledDeparture());
        assertEquals(scheduledArrival, flight.getScheduledArrival());
    }
}