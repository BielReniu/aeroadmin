package cat.uvic.teknos.dam.aeroadmin.jpa.repositories;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaFlight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class JpaFlightRepository implements FlightRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaFlightRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Flight flight) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.merge(flight); // Simplificat: gestiona la creació i l'actualització
            tx.commit();
        }
    }

    @Override
    public void delete(Flight flight) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            // Cal un cast per accedir a l'ID de la implementació de JPA
            int id = ((JpaFlight) flight).getFlightId();
            Flight toDelete = em.find(JpaFlight.class, id);

            if (toDelete != null)
                em.remove(toDelete);

            tx.commit();
        }
    }

    @Override
    public Flight get(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(JpaFlight.class, id);
        }
    }

    @Override
    public Set<Flight> getAll() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaFlight> query = em.createQuery("SELECT f FROM JpaFlight f", JpaFlight.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Set<Flight> getByDepartureAirport(String departureAirport) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaFlight> query = em.createQuery(
                    "SELECT f FROM JpaFlight f WHERE f.departureAirport = :airport",
                    JpaFlight.class
            );
            query.setParameter("airport", departureAirport);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Flight create() {
        return new JpaFlight();
    }
}