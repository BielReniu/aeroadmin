package cat.uvic.teknos.dam.aeroadmin.jpa.repositories;

import cat.uvic.teknos.dam.aeroadmin.jpa.model.JpaAircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftDetailRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class JpaAircraftDetailRepository implements AircraftDetailRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaAircraftDetailRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(AircraftDetail detail) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            if (((JpaAircraftDetail) detail).getAircraftId() == 0)
                em.persist(detail);
            else
                em.merge(detail);

            tx.commit();
        }
    }

    @Override
    public void delete(AircraftDetail detail) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            AircraftDetail toDelete = em.find(JpaAircraftDetail.class, ((JpaAircraftDetail) detail).getAircraftId());
            if (toDelete != null)
                em.remove(toDelete);

            tx.commit();
        }
    }

    @Override
    public AircraftDetail get(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(JpaAircraftDetail.class, id);
        }
    }

    @Override
    public Set<AircraftDetail> getAll() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaAircraftDetail> query = em.createQuery("SELECT d FROM JpaAircraftDetail d", JpaAircraftDetail.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Set<AircraftDetail> getByPassengerCapacity(int minCapacity, int maxCapacity) {
        return Set.of();
    }

    @Override
    public AircraftDetail create() {
        return null;
    }
}