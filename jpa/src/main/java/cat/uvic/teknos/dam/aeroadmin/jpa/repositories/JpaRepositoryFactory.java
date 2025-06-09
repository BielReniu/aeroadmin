package cat.uvic.teknos.dam.aeroadmin.jpa.repositories;

import cat.uvic.teknos.dam.aeroadmin.repositories.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaRepositoryFactory implements RepositoryFactory {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("aerodmin");

    @Override
    public AircraftRepository getAircraftRepository() {
        return new JpaAircraftRepository(emf);
    }

    @Override
    public AircraftDetailRepository getAircraftDetailRepository() {
        return new JpaAircraftDetailRepository(emf);
    }

    @Override
    public PilotRepository getPilotRepository() {
        return new JpaPilotRepository(emf);
    }

    @Override
    public PilotLicenseRepository getPilotLicenseRepository() {
        return new JpaPilotLicenseRepository(emf);
    }

    @Override
    public PilotAssignmentRepository getPilotAssignmentRepository() {
        return new JpaPilotAssignmentRepository(emf);
    }

    @Override
    public AirlineRepository getAirlineRepository() {
        return new JpaAirlineRepository(emf);
    }

    @Override
    public FlightRepository getFlightRepository() {
        return new JpaFlightRepository(emf);
    }

    //
    // Si el teu interface defineix també mètodes `createXxxRepository()`,
    // pots fer que deleguin als `getXxxRepository()`, per exemple:
    //

    @Override
    public AircraftRepository createAircraftRepository() {
        return getAircraftRepository();
    }

    @Override
    public AircraftDetailRepository createAircraftDetailRepository() {
        return getAircraftDetailRepository();
    }

    @Override
    public PilotRepository createPilotRepository() {
        return getPilotRepository();
    }

    @Override
    public PilotLicenseRepository createPilotLicenseRepository() {
        return getPilotLicenseRepository();
    }

    @Override
    public PilotAssignmentRepository createPilotAssignmentRepository() {
        return getPilotAssignmentRepository();
    }

    @Override
    public AirlineRepository createAirlineRepository() {
        return getAirlineRepository();
    }

    @Override
    public FlightRepository createFlightRepository() {
        return getFlightRepository();
    }
}
