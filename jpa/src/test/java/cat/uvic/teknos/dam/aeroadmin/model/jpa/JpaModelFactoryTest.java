package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JpaModelFactoryTest {

    private final ModelFactory modelFactory = new JpaModelFactory();

    @Test
    void shouldCreateAircraftInstance() {
        Aircraft aircraft = modelFactory.createAircraft();
        assertNotNull(aircraft);
        assertTrue(aircraft instanceof JpaAircraft);
    }

    @Test
    void shouldCreateAircraftDetailInstance() {
        AircraftDetail detail = modelFactory.createAircraftDetail();
        assertNotNull(detail);
        assertTrue(detail instanceof JpaAircraftDetail);
    }

    @Test
    void shouldCreateAirlineInstance() {
        Airline airline = modelFactory.createAirline();
        assertNotNull(airline);
        assertTrue(airline instanceof JpaAirline);
    }

    @Test
    void shouldCreatePilotInstance() {
        Pilot pilot = modelFactory.createPilot();
        assertNotNull(pilot);
        assertTrue(pilot instanceof JpaPilot);
    }

    @Test
    void shouldCreatePilotLicenseInstance() {
        PilotLicense license = modelFactory.createPilotLicense();
        assertNotNull(license);
        assertTrue(license instanceof JpaPilotLicense);
    }

    @Test
    void shouldCreatePilotAssignmentInstance() {
        PilotAssignment assignment = modelFactory.createPilotAssignment();
        assertNotNull(assignment);
        assertTrue(assignment instanceof JpaPilotAssignment);
    }

    @Test
    void shouldCreateFlightInstance() {
        Flight flight = modelFactory.createFlight();
        assertNotNull(flight);
        assertTrue(flight instanceof JpaFlight);
    }
}