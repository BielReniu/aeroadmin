package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotLicenseImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotLicenseRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPilotLicenseRepositoryTest {

    private static Connection connection;
    private PilotLicenseRepository repository;

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
        repository = new JdbcPilotLicenseRepository(new SingleConnectionDataSource());
    }

    @AfterEach
    void tearDown() throws SQLException {
        dropTable();
    }

    @Test
    void shouldSaveAndGetPilotLicense() {
        var license = new PilotLicenseImpl();
        license.setPilotId(1);
        license.setLicenseNumber("LIC123456");
        license.setLicenseType(LicenseType.ATPL);
        license.setIssueDate(LocalDate.of(2020, 1, 15));
        license.setExpirationDate(LocalDate.of(2025, 1, 15));

        repository.save(license);

        PilotLicense retrieved = repository.get(license.getPilotId());
        assertNotNull(retrieved);
        assertEquals("LIC123456", retrieved.getLicenseNumber());
        assertEquals(LicenseType.ATPL, retrieved.getLicenseType());
        assertEquals(LocalDate.of(2020, 1, 15), retrieved.getIssueDate());
        assertEquals(LocalDate.of(2025, 1, 15), retrieved.getExpirationDate());
    }

    @Test
    void shouldUpdatePilotLicense() {
        var license = new PilotLicenseImpl();
        license.setPilotId(1);
        license.setLicenseNumber("LIC123456");
        license.setLicenseType(LicenseType.CPL);
        license.setIssueDate(LocalDate.of(2019, 5, 10));
        license.setExpirationDate(LocalDate.of(2024, 5, 10));

        repository.save(license);

        license.setLicenseNumber("NEWLIC789012");
        license.setLicenseType(LicenseType.ATPL);
        license.setIssueDate(LocalDate.of(2021, 3, 20));
        license.setExpirationDate(LocalDate.of(2026, 3, 20));

        repository.save(license);

        PilotLicense updated = repository.get(license.getPilotId());

        assertNotNull(updated);
        assertEquals("NEWLIC789012", updated.getLicenseNumber());
        assertEquals(LicenseType.ATPL, updated.getLicenseType());
        assertEquals(LocalDate.of(2021, 3, 20), updated.getIssueDate());
        assertEquals(LocalDate.of(2026, 3, 20), updated.getExpirationDate());
    }

    @Test
    void shouldDeletePilotLicense() {
        var license = new PilotLicenseImpl();
        license.setPilotId(1);
        license.setLicenseNumber("DEL-LIC-001");
        license.setLicenseType(LicenseType.SPL);
        license.setIssueDate(LocalDate.of(2022, 10, 5));
        license.setExpirationDate(LocalDate.of(2027, 10, 5));

        repository.save(license);

        repository.delete(license);

        PilotLicense deleted = repository.get(license.getPilotId());
        assertNull(deleted);
    }

    @Test
    void shouldGetAllPilotLicenses() {
        var license1 = new PilotLicenseImpl();
        license1.setPilotId(1);
        license1.setLicenseNumber("LIC001");
        license1.setLicenseType(LicenseType.ATPL);
        license1.setIssueDate(LocalDate.of(2020, 1, 1));
        license1.setExpirationDate(LocalDate.of(2025, 1, 1));
        repository.save(license1);

        var license2 = new PilotLicenseImpl();
        license2.setPilotId(2);
        license2.setLicenseNumber("LIC002");
        license2.setLicenseType(LicenseType.CPL);
        license2.setIssueDate(LocalDate.of(2019, 6, 15));
        license2.setExpirationDate(LocalDate.of(2024, 6, 15));
        repository.save(license2);

        Set<PilotLicense> all = repository.getAll();
        assertEquals(2, all.size());
    }

    private void createTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE PILOT_LICENSE (
                    PILOT_ID INT PRIMARY KEY,
                    LICENSE_NUMBER VARCHAR(50),
                    LICENSE_TYPE VARCHAR(50),
                    ISSUE_DATE DATE,
                    EXPIRATION_DATE DATE
                )
                """);
        }
    }

    private void dropTable() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS PILOT_LICENSE");
        }
    }
}