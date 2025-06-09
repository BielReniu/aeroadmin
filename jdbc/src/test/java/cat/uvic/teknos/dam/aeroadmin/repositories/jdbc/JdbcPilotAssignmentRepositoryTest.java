package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import cat.uvic.teknos.dam.aeroadmin.model.model.*;
import cat.uvic.teknos.dam.aeroadmin.model.impl.*;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotAssignmentRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPilotAssignmentRepositoryTest {

    private static Connection connection;
    private PilotAssignmentRepository repository;

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Connexió a BD en memòria H2
        connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        createTables();
        repository = new JdbcPilotAssignmentRepository((DataSource) new SingleConnectionDataSource());
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    void shouldSaveAndGetPilotAssignment() {
        Airline airline = createAirline("Iberia", "IB", "IBE", "Spain", 1970, "https://iberia.com");
        Aircraft aircraft = createAircraft("737", "Boeing", "EC-ABC", 2020, airline);
        Flight flight = createFlight("IB3456", "BCN", "MAD", LocalDateTime.of(2024, 12, 10, 10, 30),
                LocalDateTime.of(2024, 12, 10, 11, 30), FlightStatus.SCHEDULED, aircraft, airline);

        Pilot pilot = createPilot("John", "Doe", "P001", "Captain", 1000);

        var assignment = new PilotAssignmentImpl();
        assignment.setRole(AssignmentRole.CAPTAIN);
        assignment.setLeadPilot(true);
        assignment.setAssignedHours(BigDecimal.valueOf(8.5));
        assignment.setFlight(flight);
        assignment.setPilot(pilot);

        repository.save(assignment);

        PilotAssignment retrieved = repository.get(assignment.getAssignmentId());

        assertNotNull(retrieved);
        assertEquals(AssignmentRole.CAPTAIN, retrieved.getRole());
        assertTrue(retrieved.isLeadPilot());
        assertEquals(BigDecimal.valueOf(8.5), retrieved.getAssignedHours());
        assertEquals("John", retrieved.getPilot().getName());
        assertEquals("IB3456", retrieved.getFlight().getFlightNumber());
    }

    @Test
    void shouldUpdatePilotAssignment() {
        Airline airline = createAirline("British Airways", "BA", "BAW", "UK", 1940, "https://ba.com");
        Aircraft aircraft = createAircraft("A320", "Airbus", "G-DEF", 2018, airline);
        Flight flight = createFlight("BA4455", "LHR", "CDG", LocalDateTime.of(2024, 12, 15, 8, 0),
                LocalDateTime.of(2024, 12, 15, 10, 0), FlightStatus.SCHEDULED, aircraft, airline);

        Pilot pilot = createPilot("Alice", "Smith", "P002", "First Officer", 800);

        var assignment = new PilotAssignmentImpl();
        assignment.setRole(AssignmentRole.FIRST_OFFICER);
        assignment.setLeadPilot(false);
        assignment.setAssignedHours(BigDecimal.valueOf(7.5));
        assignment.setFlight(flight);
        assignment.setPilot(pilot);

        repository.save(assignment);

        assignment.setRole(AssignmentRole.CAPTAIN);
        assignment.setLeadPilot(true);
        assignment.setAssignedHours(BigDecimal.valueOf(9.0));
        repository.save(assignment);

        PilotAssignment updated = repository.get(assignment.getAssignmentId());

        assertNotNull(updated);
        assertEquals(AssignmentRole.CAPTAIN, updated.getRole());
        assertTrue(updated.isLeadPilot());
        assertEquals(BigDecimal.valueOf(9.0), updated.getAssignedHours());
    }

    @Test
    void shouldDeletePilotAssignment() {
        Airline airline = createAirline("EasyJet", "EZY", "EZY", "UK", 1995, "https://easyjet.com");
        Aircraft aircraft = createAircraft("737", "Boeing", "N-XYZ", 2017, airline);
        Flight flight = createFlight("EZY1234", "LGW", "AGP", LocalDateTime.of(2024, 12, 20, 12, 0),
                LocalDateTime.of(2024, 12, 20, 14, 0), FlightStatus.ON_TIME, aircraft, airline);

        Pilot pilot = createPilot("Bob", "Johnson", "P003", "Captain", 1200);

        var assignment = new PilotAssignmentImpl();
        assignment.setRole(AssignmentRole.CAPTAIN);
        assignment.setLeadPilot(true);
        assignment.setAssignedHours(BigDecimal.valueOf(8.0));
        assignment.setFlight(flight);
        assignment.setPilot(pilot);

        repository.save(assignment);

        repository.delete(assignment);

        PilotAssignment deleted = repository.get(assignment.getAssignmentId());
        assertNull(deleted);
    }

    @Test
    void shouldGetAllPilotAssignments() {
        Airline airline1 = createAirline("Ryanair", "FR", "RYR", "Ireland", 1984, "https://ryanair.com");
        Aircraft aircraft1 = createAircraft("B737", "Boeing", "EI-FGZ", 2019, airline1);
        Flight flight1 = createFlight("FR8154", "BCN", "STN", LocalDateTime.of(2024, 12, 10, 14, 0),
                LocalDateTime.of(2024, 12, 10, 15, 30), FlightStatus.ON_TIME, aircraft1, airline1);

        Pilot pilot1 = createPilot("Emma", "Watson", "P004", "Captain", 900);

        var assignment1 = new PilotAssignmentImpl();
        assignment1.setRole(AssignmentRole.CAPTAIN);
        assignment1.setLeadPilot(true);
        assignment1.setAssignedHours(BigDecimal.valueOf(8.5));
        assignment1.setFlight(flight1);
        assignment1.setPilot(pilot1);

        Airline airline2 = createAirline("Emirates", "EK", "UAE", "UAE", 1985, "https://emirates.com");
        Aircraft aircraft2 = createAircraft("A380", "Airbus", "A6-EDB", 2016, airline2);
        Flight flight2 = createFlight("EK763", "DXB", "LHR", LocalDateTime.of(2024, 12, 10, 16, 0),
                LocalDateTime.of(2024, 12, 10, 20, 30), FlightStatus.BOARDING, aircraft2, airline2);

        Pilot pilot2 = createPilot("James", "Bond", "P005", "First Officer", 700);

        var assignment2 = new PilotAssignmentImpl();
        assignment2.setRole(AssignmentRole.FIRST_OFFICER);
        assignment2.setLeadPilot(false);
        assignment2.setAssignedHours(BigDecimal.valueOf(9.0));
        assignment2.setFlight(flight2);
        assignment2.setPilot(pilot2);

        repository.save(assignment1);
        repository.save(assignment2);

        Set<PilotAssignment> all = repository.getAll();
        assertEquals(2, all.size());
    }

    private Airline createAirline(String name, String iataCode, String icaoCode, String country, int foundationYear, String website) {
        var airline = new AirlineImpl();
        airline.setName(name);
        airline.setIataCode(iataCode);
        airline.setIcaoCode(icaoCode);
        airline.setCountry(country);
        airline.setFoundationYear(foundationYear);
        airline.setWebsite(website);
        new JdbcAirlineRepository((DataSource) new SingleConnectionDataSource()).save(airline);
        return airline;
    }

    private Aircraft createAircraft(String model, String manufacturer, String registrationNumber, int productionYear, Airline airline) {
        var aircraft = new AircraftImpl();
        aircraft.setModel(model);
        aircraft.setManufacturer(manufacturer);
        aircraft.setRegistrationNumber(registrationNumber);
        aircraft.setProductionYear(productionYear);
        aircraft.setAirline(airline);
        new JdbcAircraftRepository((DataSource) new SingleConnectionDataSource()).save(aircraft);
        return aircraft;
    }

    private Flight createFlight(String flightNumber, String departureAirport, String arrivalAirport,
                                LocalDateTime scheduledDeparture, LocalDateTime scheduledArrival,
                                FlightStatus status, Aircraft aircraft, Airline airline) {
        var flight = new FlightImpl();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setScheduledDeparture(scheduledDeparture);
        flight.setScheduledArrival(scheduledArrival);
        flight.setStatus(status);
        flight.setAircraft(aircraft);
        flight.setAirline(airline);
        new JdbcFlightRepository((DataSource) new SingleConnectionDataSource()).save(flight);
        return flight;
    }

    private Pilot createPilot(String name, String surname, String licenseNumber, String role, int experienceYears) {
        var pilot = new PilotImpl();
        pilot.setName(name);
        pilot.setSurname(surname);
        pilot.setLicenseNumber(licenseNumber);
        pilot.setRole(role);
        pilot.setExperienceYears(experienceYears);
        new JdbcPilotRepository((DataSource) new SingleConnectionDataSource()).save(pilot);
        return pilot;
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE AIRLINE (
                    AIRLINE_ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(255),
                    IATA_CODE VARCHAR(10),
                    ICAO_CODE VARCHAR(10),
                    COUNTRY VARCHAR(100),
                    FOUNDATION_YEAR INT,
                    WEBSITE VARCHAR(255)
                )
                """);

            stmt.execute("""
                CREATE TABLE AIRCRAFT (
                    AIRCRAFT_ID INT PRIMARY KEY AUTO_INCREMENT,
                    MODEL VARCHAR(255),
                    MANUFACTURER VARCHAR(255),
                    REGISTRATION_NUMBER VARCHAR(20),
                    PRODUCTION_YEAR INT,
                    AIRLINE_ID INT,
                    FOREIGN KEY (AIRLINE_ID) REFERENCES AIRLINE(AIRLINE_ID)
                )
                """);

            stmt.execute("""
                CREATE TABLE FLIGHT (
                    FLIGHT_ID INT PRIMARY KEY AUTO_INCREMENT,
                    FLIGHT_NUMBER VARCHAR(20),
                    DEPARTURE_AIRPORT VARCHAR(10),
                    ARRIVAL_AIRPORT VARCHAR(10),
                    SCHEDULED_DEPARTURE TIMESTAMP,
                    SCHEDULED_ARRIVAL TIMESTAMP,
                    STATUS VARCHAR(20),
                    AIRCRAFT_ID INT,
                    AIRLINE_ID INT,
                    FOREIGN KEY (AIRCRAFT_ID) REFERENCES AIRCRAFT(AIRCRAFT_ID),
                    FOREIGN KEY (AIRLINE_ID) REFERENCES AIRLINE(AIRLINE_ID)
                )
                """);

            stmt.execute("""
                CREATE TABLE PILOT (
                    PILOT_ID INT PRIMARY KEY AUTO_INCREMENT,
                    NAME VARCHAR(100),
                    SURNAME VARCHAR(100),
                    LICENSE_NUMBER VARCHAR(50),
                    ROLE VARCHAR(50),
                    EXPERIENCE_YEARS INT
                )
                """);

            stmt.execute("""
                CREATE TABLE PILOT_ASSIGNMENT (
                    ASSIGNMENT_ID INT PRIMARY KEY AUTO_INCREMENT,
                    FLIGHT_ID INT,
                    PILOT_ID INT,
                    ROLE VARCHAR(50),
                    LEAD_PILOT BOOLEAN,
                    ASSIGNED_HOURS DECIMAL(10,2),
                    FOREIGN KEY (FLIGHT_ID) REFERENCES FLIGHT(FLIGHT_ID),
                    FOREIGN KEY (PILOT_ID) REFERENCES PILOT(PILOT_ID)
                )
                """);
        }
    }

    private void dropTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS PILOT_ASSIGNMENT");
            stmt.execute("DROP TABLE IF EXISTS PILOT");
            stmt.execute("DROP TABLE IF EXISTS FLIGHT");
            stmt.execute("DROP TABLE IF EXISTS AIRCRAFT");
            stmt.execute("DROP TABLE IF EXISTS AIRLINE");
        }
    }
}