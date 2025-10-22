package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftImpl;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcAircraftRepositoryTest {

    private static Connection connection;
    private AircraftRepository repository;

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
        repository = new JdbcAircraftRepository((DataSource) new SingleConnectionDataSource());
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTables();
    }

    @Test
    void shouldSaveAndGetAircraft() {
        Airline airline = new AirlineImpl();
        airline.setAirlineId(1);
        airline.setName("Iberia");
        airline.setCode("IB");

        var aircraft = new AircraftImpl();
        aircraft.setModel("737");
        aircraft.setManufacturer("Boeing");
        aircraft.setRegistrationNumber("EC-ABC");
        aircraft.setProductionYear(2020);
        aircraft.setAirline(airline);

        repository.save(aircraft);

        Aircraft retrieved = repository.get(aircraft.getAircraftId());
        assertNotNull(retrieved);
        assertEquals("Boeing", retrieved.getManufacturer());
        assertEquals("EC-ABC", retrieved.getRegistrationNumber());
        assertEquals(2020, retrieved.getProductionYear());
        assertEquals("Iberia", retrieved.getAirline().getName());
    }

    @Test
    void shouldUpdateAircraft() {
        Airline airline = new AirlineImpl();
        airline.setAirlineId(1);
        airline.setName("British Airways");
        airline.setCode("BA");

        var aircraft = new AircraftImpl();
        aircraft.setModel("A320");
        aircraft.setManufacturer("Airbus");
        aircraft.setRegistrationNumber("G-DEF");
        aircraft.setProductionYear(2018);
        aircraft.setAirline(airline);

        repository.save(aircraft);

        aircraft.setManufacturer("Boeing");
        aircraft.setModel("747");
        repository.save(aircraft);

        Aircraft updated = repository.get(aircraft.getAircraftId());

        assertNotNull(updated);
        assertEquals("Boeing", updated.getManufacturer());
        assertEquals("747", updated.getModel());
    }

    @Test
    void shouldDeleteAircraft() {
        Airline airline = new AirlineImpl();
        airline.setAirlineId(1);
        airline.setName("Lufthansa");
        airline.setCode("LH");

        var aircraft = new AircraftImpl();
        aircraft.setModel("A380");
        aircraft.setManufacturer("Airbus");
        aircraft.setRegistrationNumber("D-GHUM");
        aircraft.setProductionYear(2019);
        aircraft.setAirline(airline);

        repository.save(aircraft);

        repository.delete(aircraft);

        Aircraft deleted = repository.get(aircraft.getAircraftId());
        assertNull(deleted);
    }

    @Test
    void shouldGetAllAircrafts() {
        Airline airline = new AirlineImpl();
        airline.setAirlineId(1);
        airline.setName("EasyJet");
        airline.setCode("EZY");

        var aircraft1 = new AircraftImpl();
        aircraft1.setModel("A320");
        aircraft1.setManufacturer("Airbus");
        aircraft1.setRegistrationNumber("G-EZYZ");
        aircraft1.setProductionYear(2021);
        aircraft1.setAirline(airline);

        var aircraft2 = new AircraftImpl();
        aircraft2.setModel("737");
        aircraft2.setManufacturer("Boeing");
        aircraft2.setRegistrationNumber("N-XYZ");
        aircraft2.setProductionYear(2017);
        aircraft2.setAirline(airline);

        repository.save(aircraft1);
        repository.save(aircraft2);

        Set<Aircraft> all = repository.getAll();
        assertEquals(2, all.size());
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE AIRLINE (
                    AIRLINE_ID INT PRIMARY KEY,
                    NAME VARCHAR(255),
                    CODE VARCHAR(10)
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
        }
    }

    private void dropTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS AIRCRAFT");
            stmt.execute("DROP TABLE IF EXISTS AIRLINE");
        }
    }
}