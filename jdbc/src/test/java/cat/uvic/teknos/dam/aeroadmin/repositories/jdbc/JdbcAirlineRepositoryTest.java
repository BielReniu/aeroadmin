package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcAirlineRepositoryTest {

    private static Connection connection;
    private AirlineRepository repository;

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
        createTable();
        repository = new JdbcAirlineRepository((DataSource) new SingleConnectionDataSource());
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTable();
    }

    @Test
    void shouldSaveAndGetAirline() {
        var airline = new AirlineImpl();
        airline.setName("Iberia");
        airline.setIataCode("IB");
        airline.setIcaoCode("IBE");
        airline.setCountry("Spain");
        airline.setFoundationYear(1970);
        airline.setWebsite("https://www.iberia.com");

        repository.save(airline);

        Airline retrieved = repository.get(airline.getAirlineId());
        assertNotNull(retrieved);
        assertEquals("Iberia", retrieved.getName());
        assertEquals("IB", retrieved.getIataCode());
        assertEquals("IBE", retrieved.getIcaoCode());
        assertEquals("Spain", retrieved.getCountry());
        assertEquals(1970, retrieved.getFoundationYear().intValue());
        assertEquals("https://www.iberia.com",  retrieved.getWebsite());
    }

    @Test
    void shouldUpdateAirline() {
        var airline = new AirlineImpl();
        airline.setName("British Airways");
        airline.setIataCode("BA");
        airline.setIcaoCode("BAW");
        airline.setCountry("UK");
        airline.setFoundationYear(1940);
        airline.setWebsite("https://www.britishairways.com");

        repository.save(airline);

        airline.setName("EasyJet");
        airline.setIataCode("EZY");
        airline.setIcaoCode("EZY");
        airline.setCountry("UK");
        airline.setFoundationYear(1995);
        airline.setWebsite("https://www.easyjet.com");

        repository.save(airline);

        Airline updated = repository.get(airline.getAirlineId());

        assertNotNull(updated);
        assertEquals("EasyJet", updated.getName());
        assertEquals("EZY", updated.getIataCode());
        assertEquals("EZY", updated.getIcaoCode());
        assertEquals(1995, updated.getFoundationYear().intValue());
        assertEquals("https://www.easyjet.com",  updated.getWebsite());
    }

    @Test
    void shouldDeleteAirline() {
        var airline = new AirlineImpl();
        airline.setName("Lufthansa");
        airline.setIataCode("LH");
        airline.setIcaoCode("DLH");
        airline.setCountry("Germany");
        airline.setFoundationYear(1953);
        airline.setWebsite("https://www.lufthansa.com");

        repository.save(airline);

        repository.delete(airline);

        Airline deleted = repository.get(airline.getAirlineId());
        assertNull(deleted);
    }

    @Test
    void shouldGetAllAirlines() {
        var airline1 = new AirlineImpl();
        airline1.setName("Ryanair");
        airline1.setIataCode("FR");
        airline1.setIcaoCode("RYR");
        airline1.setCountry("Ireland");
        airline1.setFoundationYear(1984);
        airline1.setWebsite("https://www.ryanair.com");
        repository.save(airline1);

        var airline2 = new AirlineImpl();
        airline2.setName("Emirates");
        airline2.setIataCode("EK");
        airline2.setIcaoCode("UAE");
        airline2.setCountry("UAE");
        airline2.setFoundationYear(1985);
        airline2.setWebsite("https://www.emirates.com");
        repository.save(airline2);

        Set<Airline> all = repository.getAll();
        assertEquals(2, all.size());
    }

    private void createTable() throws SQLException {
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
        }
    }

    private void dropTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS AIRLINE");
        }
    }
}