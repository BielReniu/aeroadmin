package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.repositories.*;
import cat.uvic.teknos.dam.aeroadmin.repositories.RepositoryFactory;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

public class JdbcRepositoryFactory implements RepositoryFactory {

    private final DataSource dataSource;

    public JdbcRepositoryFactory() {
        this.dataSource = new SingleConnectionDataSource();
    }

    @Override
    public AircraftDetailRepository getAircraftDetailRepository() {
        return new JdbcAircraftDetailRepository(dataSource);
    }

    @Override
    public AircraftRepository getAircraftRepository() {
        return new JdbcAircraftRepository(dataSource);
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
    public PilotAssignmentRepository getPilotAssignmentRepository() {
        return new JdbcPilotAssignmentRepository(dataSource);
    }

    @Override
    public PilotLicenseRepository getPilotLicenseRepository() {
        return new JdbcPilotLicenseRepository(dataSource);
    }

    @Override
    public PilotRepository getPilotRepository() {
        return new JdbcPilotRepository(dataSource);
    }

    @Override
    public AircraftRepository createAircraftRepository() {
        return null;
    }

    @Override
    public AircraftDetailRepository createAircraftDetailRepository() {
        return null;
    }

    @Override
    public AirlineRepository createAirlineRepository() {
        return null;
    }

    @Override
    public PilotRepository createPilotRepository() {
        return null;
    }

    @Override
    public PilotLicenseRepository createPilotLicenseRepository() {
        return null;
    }

    @Override
    public PilotAssignmentRepository createPilotAssignmentRepository() {
        return null;
    }

    @Override
    public FlightRepository createFlightRepository() {
        return null;
    }
}