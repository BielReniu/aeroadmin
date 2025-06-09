package cat.uvic.teknos.dam.aeroadmin.repositories.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.jpa.JpaPilot;
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

            if (((JpaPilot) pilot).getPilotId() == 0)
                em.persist(pilot);
            else
                em.merge(pilot);

            tx.commit();
        }
    }

    @Override
    public void delete(Pilot pilot) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            Pilot toDelete = em.find(JpaPilot.class, ((JpaPilot) pilot).getPilotId());
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
        return Set.of();
    }
}