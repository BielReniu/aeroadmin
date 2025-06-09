package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaAirline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JpaAirlineTest {

    @Test
    void shouldSetAndGetProperties() {
        Airline airline = new JpaAirline();

        airline.setAirlineId(1);
        airline.setName("Iberia");
        airline.setIataCode("IB");
        airline.setIcaoCode("IBE");
        airline.setCountry("Spain");
        airline.setFoundationYear(1960);
        airline.setWebsite("https://iberia.com");

        assertEquals(1, airline.getAirlineId());
        assertEquals("Iberia", airline.getName());
        assertEquals("IB", airline.getIataCode());
        assertEquals("IBE", airline.getIcaoCode());
        assertEquals("Spain", airline.getCountry());
        assertEquals(Integer.valueOf(1960), airline.getFoundationYear());
        assertEquals("https://iberia.com",  airline.getWebsite());
    }
}