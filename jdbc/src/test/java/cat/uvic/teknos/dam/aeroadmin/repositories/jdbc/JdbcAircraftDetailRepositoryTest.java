package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftDetailImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftDetailRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.JdbcAircraftDetailRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcAircraftDetailRepositoryTest {

    private static Connection connection;
    private AircraftDetailRepository repository;

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
        repository = new JdbcAircraftDetailRepository(new SingleConnectionDataSource());
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTable();
    }

    @Test
    void shouldSaveAndRetrieveAircraftDetail() {
        AircraftDetail detail = new AircraftDetailImpl();
        detail.setAircraftId(1);
        detail.setPassengerCapacity(200);
        detail.setMaxRangeKm(8000);
        detail.setMaxSpeedKmh(900);
        detail.setFuelCapacityLiters(100000);

        repository.save(detail);

        AircraftDetail retrieved = repository.get(1);
        assertNotNull(retrieved);
        assertEquals(200, retrieved.getPassengerCapacity());
        assertEquals(8000, retrieved.getMaxRangeKm());
        assertEquals(900, retrieved.getMaxSpeedKmh());
        assertEquals(100000, retrieved.getFuelCapacityLiters());
    }

    @Test
    void shouldUpdateAircraftDetail() {
        AircraftDetail detail = new AircraftDetailImpl();
        detail.setAircraftId(1);
        detail.setPassengerCapacity(100);
        detail.setMaxRangeKm(5000);
        detail.setMaxSpeedKmh(800);
        detail.setFuelCapacityLiters(80000);
        repository.save(detail);

        detail.setPassengerCapacity(150);
        detail.setMaxRangeKm(6000);
        repository.save(detail);

        AircraftDetail updated = repository.get(1);
        assertEquals(150, updated.getPassengerCapacity());
        assertEquals(6000, updated.getMaxRangeKm());
    }

    @Test
    void shouldDeleteAircraftDetail() {
        AircraftDetail detail = new AircraftDetailImpl();
        detail.setAircraftId(1);
        detail.setPassengerCapacity(100);
        detail.setMaxRangeKm(5000);
        detail.setMaxSpeedKmh(800);
        detail.setFuelCapacityLiters(80000);
        repository.save(detail);

        repository.delete(detail);

        AircraftDetail deleted = repository.get(1);
        assertNull(deleted);
    }

    @Test
    void shouldGetAllAircraftDetails() {
        AircraftDetail detail1 = new AircraftDetailImpl();
        detail1.setAircraftId(1);
        detail1.setPassengerCapacity(150);
        detail1.setMaxRangeKm(7000);
        detail1.setMaxSpeedKmh(850);
        detail1.setFuelCapacityLiters(90000);

        AircraftDetail detail2 = new AircraftDetailImpl();
        detail2.setAircraftId(2);
        detail2.setPassengerCapacity(180);
        detail2.setMaxRangeKm(7500);
        detail2.setMaxSpeedKmh(870);
        detail2.setFuelCapacityLiters(95000);

        repository.save(detail1);
        repository.save(detail2);

        Set<AircraftDetail> all = repository.getAll();
        assertEquals(2, all.size());
    }

    private void createTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE AIRCRAFT_DETAIL (
                    AIRCRAFT_ID INT PRIMARY KEY,
                    PASSENGER_CAPACITY INT,
                    MAX_RANGE_KM INT,
                    MAX_SPEED_KMH INT,
                    FUEL_CAPACITY_LITERS INT
                )
                """);
        }
    }

    private void dropTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS AIRCRAFT_DETAIL");
        }
    }
}