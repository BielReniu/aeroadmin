package cat.uvic.teknos.dam.aeroadmin.jpa.repositories;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaAirline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class JpaAirlineRepository implements AirlineRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaAirlineRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Airline airline) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            if (((JpaAirline) airline).getAirlineId() == 0)
                em.persist(airline);
            else
                em.merge(airline);

            tx.commit();
        }
    }

    @Override
    public void delete(Airline airline) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            Airline toDelete = em.find(JpaAirline.class, ((JpaAirline) airline).getAirlineId());
            if (toDelete != null)
                em.remove(toDelete);

            tx.commit();
        }
    }

    @Override
    public Airline get(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(JpaAirline.class, id);
        }
    }

    @Override
    public Set<Airline> getAll() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaAirline> query = em.createQuery("SELECT a FROM JpaAirline a", JpaAirline.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Airline get(int id) {
        return null;
    }

    @Override
    public Set<Airline> getByCountry(String country) {
        return Set.of();
    }

    @Override
    public Airline create() {
        return null;
    }
}