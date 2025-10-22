package cat.uvic.teknos.dam.aeroadmin.jpa.repositories;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaPilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class JpaPilotRepository implements PilotRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaPilotRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Pilot pilot) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.merge(pilot); // Simplificat: gestiona la creació i l'actualització
            tx.commit();
        }
    }

    @Override
    public void delete(Pilot pilot) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            // Cal un cast per accedir a l'ID de la implementació de JPA
            int id = ((JpaPilot) pilot).getPilotId();
            Pilot toDelete = em.find(JpaPilot.class, id);

            if (toDelete != null)
                em.remove(toDelete);

            tx.commit();
        }
    }

    @Override
    public Pilot get(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(JpaPilot.class, id);
        }
    }

    @Override
    public Set<Pilot> getAll() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaPilot> query = em.createQuery("SELECT p FROM JpaPilot p", JpaPilot.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Set<Pilot> getByAirline(Airline airline) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaPilot> query = em.createQuery(
                    // JPQL permet filtrar per l'entitat relacionada directament
                    "SELECT p FROM JpaPilot p WHERE p.airline = :airline",
                    JpaPilot.class
            );
            query.setParameter("airline", airline);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Pilot create() {
        return new JpaPilot();
    }
}