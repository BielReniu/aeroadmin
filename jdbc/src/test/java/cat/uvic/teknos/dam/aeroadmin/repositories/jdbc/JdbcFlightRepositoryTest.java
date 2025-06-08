package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import cat.uvic.teknos.dam.aeroadmin.model.model.*;
import cat.uvic.teknos.dam.aeroadmin.model.impl.FlightImpl;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcFlightRepositoryTest {

    private static Connection connection;
    private FlightRepository repository;

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
        repository = new JdbcFlightRepository(new SingleConnectionDataSource());
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    void shouldSaveAndGetFlight() {
        Airline airline = createAirline("Iberia", "IB", "IBE", "Spain", 1970, "https://iberia.com");
        Aircraft aircraft = createAircraft("737", "Boeing", "EC-ABC", 2020, airline);

        var flight = new FlightImpl();
        flight.setFlightNumber("IB3456");
        flight.setDepartureAirport("BCN");
        flight.setArrivalAirport("MAD");
        flight.setScheduledDeparture(LocalDateTime.of(2024, 12, 10, 10, 30));
        flight.setScheduledArrival(LocalDateTime.of(2024, 12, 10, 11, 30));
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setAirline(airline);
        flight.setAircraft(aircraft);

        repository.save(flight);

        Flight retrieved = repository.get(flight.getFlightId());
        assertNotNull(retrieved);
        assertEquals("IB3456", retrieved.getFlightNumber());
        assertEquals("BCN", retrieved.getDepartureAirport());
        assertEquals("MAD", retrieved.getArrivalAirport());
        assertEquals(LocalDateTime.of(2024, 12, 10, 10, 30), retrieved.getScheduledDeparture());
        assertEquals(LocalDateTime.of(2024, 12, 10, 11, 30), retrieved.getScheduledArrival());
        assertEquals(FlightStatus.SCHEDULED, retrieved.getStatus());
        assertEquals("Iberia", retrieved.getAirline().getName());
        assertEquals("Boeing", retrieved.getAircraft().getManufacturer());
    }

    @Test
    void shouldUpdateFlight() {
        Airline airline = createAirline("British Airways", "BA", "BAW", "UK", 1940, "https://ba.com");
        Aircraft aircraft = createAircraft("A320", "Airbus", "G-DEF", 2018, airline);

        var flight = new FlightImpl();
        flight.setFlightNumber("BA4455");
        flight.setDepartureAirport("LHR");
        flight.setArrivalAirport("CDG");
        flight.setScheduledDeparture(LocalDateTime.of(2024, 12, 15, 8, 0));
        flight.setScheduledArrival(LocalDateTime.of(2024, 12, 15, 10, 0));
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setAirline(airline);
        flight.setAircraft(aircraft);

        repository.save(flight);

        flight.setFlightNumber("BA4466");
        flight.setDepartureAirport("LGW");
        flight.setArrivalAirport("AMS");
        flight.setScheduledDeparture(LocalDateTime.of(2024, 12, 16, 9, 0));
        flight.setScheduledArrival(LocalDateTime.of(2024, 12, 16, 11, 0));
        flight.setStatus(FlightStatus.DELAYED);
        repository.save(flight);

        Flight updated = repository.get(flight.getFlightId());

        assertNotNull(updated);
        assertEquals("BA4466", updated.getFlightNumber());
        assertEquals("LGW", updated.getDepartureAirport());
        assertEquals("AMS", updated.getArrivalAirport());
        assertEquals(LocalDateTime.of(2024, 12, 16, 9, 0), updated.getScheduledDeparture());
        assertEquals(LocalDateTime.of(2024, 12, 16, 11, 0), updated.getScheduledArrival());
        assertEquals(FlightStatus.DELAYED, updated.getStatus());
    }

    @Test
    void shouldDeleteFlight() {
        Airline airline = createAirline("EasyJet", "EZY", "EZY", "UK", 1995, "https://easyjet.com");
        Aircraft aircraft = createAircraft("737", "Boeing", "N-XYZ", 2017, airline);

        var flight = new FlightImpl();
        flight.setFlightNumber("EZY1234");
        flight.setDepartureAirport("LGW");
        flight.setArrivalAirport("AGP");
        flight.setScheduledDeparture(LocalDateTime.of(2024, 12, 20, 12, 0));
        flight.setScheduledArrival(LocalDateTime.of(2024, 12, 20, 14, 0));
        flight.setStatus(FlightStatus.ON_TIME);
        flight.setAirline(airline);
        flight.setAircraft(aircraft);

        repository.save(flight);

        repository.delete(flight);

        Flight deleted = repository.get(flight.getFlightId());
        assertNull(deleted);
    }

    @Test
    void shouldGetAllFlights() {
        Airline airline1 = createAirline("Ryanair", "FR", "RYR", "Ireland", 1984, "https://ryanair.com");
        Aircraft aircraft1 = createAircraft("B737", "Boeing", "EI-FGZ", 2019, airline1);

        var flight1 = new FlightImpl();
        flight1.setFlightNumber("FR8154");
        flight1.setDepartureAirport("BCN");
        flight1.setArrivalAirport("STN");
        flight1.setScheduledDeparture(LocalDateTime.of(2024, 12, 10, 14, 0));
        flight1.setScheduledArrival(LocalDateTime.of(2024, 12, 10, 15, 30));
        flight1.setStatus(FlightStatus.ON_TIME);
        flight1.setAirline(airline1);
        flight1.setAircraft(aircraft1);

        Airline airline2 = createAirline("Emirates", "EK", "UAE", "UAE", 1985, "https://emirates.com");
        Aircraft aircraft2 = createAircraft("A380", "Airbus", "A6-EDB", 2016, airline2);

        var flight2 = new FlightImpl();
        flight2.setFlightNumber("EK763");
        flight2.setDepartureAirport("DXB");
        flight2.setArrivalAirport("LHR");
        flight2.setScheduledDeparture(LocalDateTime.of(2024, 12, 10, 16, 0));
        flight2.setScheduledArrival(LocalDateTime.of(2024, 12, 10, 20, 30));
        flight2.setStatus(FlightStatus.BOARDING);
        flight2.setAirline(airline2);
        flight2.setAircraft(aircraft2);

        repository.save(flight1);
        repository.save(flight2);

        Set<Flight> all = repository.getAll();
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
        new JdbcAirlineRepository(new SingleConnectionDataSource()).save(airline);
        return airline;
    }

    private Aircraft createAircraft(String model, String manufacturer, String registrationNumber, int productionYear, Airline airline) {
        var aircraft = new AircraftImpl();
        aircraft.setModel(model);
        aircraft.setManufacturer(manufacturer);
        aircraft.setRegistrationNumber(registrationNumber);
        aircraft.setProductionYear(productionYear);
        aircraft.setAirline(airline);
        new JdbcAircraftRepository(new SingleConnectionDataSource()).save(aircraft);
        return aircraft;
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
        }
    }

    private void dropTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS FLIGHT");
            stmt.execute("DROP TABLE IF EXISTS AIRCRAFT");
            stmt.execute("DROP TABLE IF EXISTS AIRLINE");
        }
    }
}