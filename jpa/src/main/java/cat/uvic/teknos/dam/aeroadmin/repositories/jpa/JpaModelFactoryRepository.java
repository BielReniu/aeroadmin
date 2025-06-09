package cat.uvic.teknos.dam.aeroadmin.repositories.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.jpa.*;
import cat.uvic.teknos.dam.aeroadmin.model.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class JpaModelFactoryRepository {

    private final EntityManagerFactory entityManagerFactory;
    private final ModelFactory modelFactory;

    public JpaModelFactoryRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.modelFactory = new JpaModelFactory();
    }

    // ========== Aircraft ==========
    public Aircraft createAndSaveAircraft() {
        Aircraft aircraft = modelFactory.createAircraft();
        persist(aircraft);
        return aircraft;
    }

    // ========== AircraftDetail ==========
    public AircraftDetail createAndSaveAircraftDetail() {
        AircraftDetail detail = modelFactory.createAircraftDetail();
        persist(detail);
        return detail;
    }

    // ========== Airline ==========
    public Airline createAndSaveAirline() {
        Airline airline = modelFactory.createAirline();
        persist(airline);
        return airline;
    }

    // ========== Pilot ==========
    public Pilot createAndSavePilot() {
        Pilot pilot = modelFactory.createPilot();
        persist(pilot);
        return pilot;
    }

    // ========== PilotLicense ==========
    public PilotLicense createAndSavePilotLicense() {
        PilotLicense license = modelFactory.createPilotLicense();
        persist(license);
        return license;
    }

    // ========== PilotAssignment ==========
    public PilotAssignment createAndSavePilotAssignment() {
        PilotAssignment assignment = modelFactory.createPilotAssignment();
        persist(assignment);
        return assignment;
    }

    // ========== Flight ==========
    public Flight createAndSaveFlight() {
        Flight flight = modelFactory.createFlight();
        persist(flight);
        return flight;
    }

    // ========== Helpers ==========

    private void persist(Object entity) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            em.persist(entity);

            tx.commit();
        }
    }
}