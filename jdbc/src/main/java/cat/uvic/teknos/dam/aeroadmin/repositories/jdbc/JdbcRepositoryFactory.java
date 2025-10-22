package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.repositories.*;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JdbcRepositoryFactory implements RepositoryFactory {

    private final DataSource dataSource;

    public JdbcRepositoryFactory() {
        Properties props = new Properties();
        try (InputStream input = JdbcRepositoryFactory.class.getResourceAsStream("/datasource.properties")) {
            if (input == null) {
                throw new RuntimeException("No s'ha trobat el fitxer datasource.properties");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error en llegir el fitxer datasource.properties", ex);
        }

        this.dataSource = new SingleConnectionDataSource(props);
    }

    @Override
    public AircraftRepository getAircraftRepository() {
        return new JdbcAircraftRepository(dataSource);
    }

    @Override
    public AircraftDetailRepository getAircraftDetailRepository() {
        return new JdbcAircraftDetailRepository(dataSource);
    }

    @Override
    public AirlineRepository getAirlineRepository() {
        return new JdbcAirlineRepository(dataSource);
    }

    @Override
    public FlightRepository getFlightRepository() {
        return new JdbcFlightRepository(dataSource);
    }

    @Override
    public PilotRepository getPilotRepository() {
        return new JdbcPilotRepository(dataSource);
    }

    @Override
    public PilotLicenseRepository getPilotLicenseRepository() {
        return new JdbcPilotLicenseRepository(dataSource);
    }

    @Override
    public PilotAssignmentRepository getPilotAssignmentRepository() {
        return new JdbcPilotAssignmentRepository(dataSource);
    }
}