package cat.uvic.teknos.dam.aeroadmin.repositories.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.jpa.JpaPilotAssignment;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotAssignmentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class JpaPilotAssignmentRepository implements PilotAssignmentRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaPilotAssignmentRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(PilotAssignment assignment) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            if (((JpaPilotAssignment) assignment).getAssignmentId() == 0)
                em.persist(assignment);
            else
                em.merge(assignment);

            tx.commit();
        }
    }

    @Override
    public void delete(PilotAssignment assignment) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            PilotAssignment toDelete = em.find(JpaPilotAssignment.class, ((JpaPilotAssignment) assignment).getAssignmentId());
            if (toDelete != null)
                em.remove(toDelete);

            tx.commit();
        }
    }

    @Override
    public PilotAssignment get(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(JpaPilotAssignment.class, id);
        }
    }

    @Override
    public Set<PilotAssignment> getAll() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaPilotAssignment> query = em.createQuery("SELECT a FROM JpaPilotAssignment a", JpaPilotAssignment.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Set<PilotAssignment> getByFlight(Flight flight) {
        return Set.of();
    }
}