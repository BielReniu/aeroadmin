package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.*;
import cat.uvic.teknos.dam.aeroadmin.model.impl.*;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPilotRepositoryTest {

    private static Connection connection;
    private PilotRepository repository;

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
        repository = new JdbcPilotRepository(new SingleConnectionDataSource());
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    void shouldSaveAndGetPilot() {
        Airline airline = createAirline("Iberia", "IB", "IBE", "Spain", 1970, "https://iberia.com");

        var pilot = new PilotImpl();
        pilot.setFirstName("John");
        pilot.setLastName("Doe");
        pilot.setDateOfBirth(LocalDate.of(1985, 5, 15));
        pilot.setNationality("Spanish");
        pilot.setAirline(airline);

        repository.save(pilot);

        Pilot retrieved = repository.get(pilot.getPilotId());
        assertNotNull(retrieved);
        assertEquals("John", retrieved.getFirstName());
        assertEquals("Doe", retrieved.getLastName());
        assertEquals(LocalDate.of(1985, 5, 15), retrieved.getDateOfBirth());
        assertEquals("Spanish", retrieved.getNationality());
        assertEquals("Iberia", retrieved.getAirline().getName());
    }

    @Test
    void shouldUpdatePilot() {
        Airline airline = createAirline("British Airways", "BA", "BAW", "UK", 1940, "https://ba.com");

        var pilot = new PilotImpl();
        pilot.setFirstName("Alice");
        pilot.setLastName("Smith");
        pilot.setDateOfBirth(LocalDate.of(1990, 8, 20));
        pilot.setNationality("British");
        pilot.setAirline(airline);

        repository.save(pilot);

        pilot.setFirstName("Robert");
        pilot.setLastName("Brown");
        pilot.setDateOfBirth(LocalDate.of(1991, 9, 21));
        pilot.setNationality("Canadian");
        Airline newAirline = createAirline("Air Canada", "AC", "ACA", "Canada", 1939, "https://aircanada.com");
        pilot.setAirline(newAirline);

        repository.save(pilot);

        Pilot updated = repository.get(pilot.getPilotId());

        assertNotNull(updated);
        assertEquals("Robert", updated.getFirstName());
        assertEquals("Brown", updated.getLastName());
        assertEquals(LocalDate.of(1991, 9, 21), updated.getDateOfBirth());
        assertEquals("Canadian", updated.getNationality());
        assertEquals("Air Canada", updated.getAirline().getName());
    }

    @Test
    void shouldDeletePilot() {
        Airline airline = createAirline("EasyJet", "EZY", "EZY", "UK", 1995, "https://easyjet.com");

        var pilot = new PilotImpl();
        pilot.setFirstName("Bob");
        pilot.setLastName("Johnson");
        pilot.setDateOfBirth(LocalDate.of(1980, 12, 10));
        pilot.setNationality("British");
        pilot.setAirline(airline);

        repository.save(pilot);

        repository.delete(pilot);

        Pilot deleted = repository.get(pilot.getPilotId());
        assertNull(deleted);
    }

    @Test
    void shouldGetAllPilots() {
        Airline airline1 = createAirline("Ryanair", "FR", "RYR", "Ireland", 1984, "https://ryanair.com");

        var pilot1 = new PilotImpl();
        pilot1.setFirstName("Emma");
        pilot1.setLastName("Watson");
        pilot1.setDateOfBirth(LocalDate.of(1988, 3, 15));
        pilot1.setNationality("Irish");
        pilot1.setAirline(airline1);

        Airline airline2 = createAirline("Emirates", "EK", "UAE", "UAE", 1985, "https://emirates.com");

        var pilot2 = new PilotImpl();
        pilot2.setFirstName("James");
        pilot2.setLastName("Bond");
        pilot2.setDateOfBirth(LocalDate.of(1975, 10, 5));
        pilot2.setNationality("Emirati");
        pilot2.setAirline(airline2);

        repository.save(pilot1);
        repository.save(pilot2);

        Set<Pilot> all = repository.getAll();
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
                CREATE TABLE PILOT (
                    PILOT_ID INT PRIMARY KEY AUTO_INCREMENT,
                    FIRST_NAME VARCHAR(100),
                    LAST_NAME VARCHAR(100),
                    DATE_OF_BIRTH DATE,
                    NATIONALITY VARCHAR(100),
                    AIRLINE_ID INT,
                    FOREIGN KEY (AIRLINE_ID) REFERENCES AIRLINE(AIRLINE_ID)
                )
                """);
        }
    }

    private void dropTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS PILOT");
            stmt.execute("DROP TABLE IF EXISTS AIRLINE");
        }
    }
}