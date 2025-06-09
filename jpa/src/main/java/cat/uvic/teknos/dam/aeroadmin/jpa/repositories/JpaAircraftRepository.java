package cat.uvic.teknos.dam.aeroadmin.jpa.repositories;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaAircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class JpaAircraftRepository implements AircraftRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaAircraftRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Aircraft aircraft) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            if (((JpaAircraft) aircraft).getAircraftId() == 0)
                em.persist(aircraft);
            else
                em.merge(aircraft);

            tx.commit();
        }
    }

    @Override
    public void delete(Aircraft aircraft) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            Aircraft toDelete = em.find(JpaAircraft.class, ((JpaAircraft) aircraft).getAircraftId());
            if (toDelete != null)
                em.remove(toDelete);

            tx.commit();
        }
    }

    @Override
    public Aircraft get(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(JpaAircraft.class, id);
        }
    }

    @Override
    public Set<Aircraft> getAll() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaAircraft> query = em.createQuery("SELECT a FROM JpaAircraft a", JpaAircraft.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Set<Aircraft> getByManufacturer(String manufacturer) {
        return Set.of();
    }

    @Override
    public Aircraft create() {
        return null;
    }
}