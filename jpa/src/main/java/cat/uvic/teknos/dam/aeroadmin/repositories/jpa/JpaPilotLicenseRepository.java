package cat.uvic.teknos.dam.aeroadmin.repositories.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.jpa.JpaPilotLicense;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotLicenseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class JpaPilotLicenseRepository implements PilotLicenseRepository {

    private final EntityManagerFactory entityManagerFactory;

    public JpaPilotLicenseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(PilotLicense license) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            if (((JpaPilotLicense) license).getPilotId() == 0)
                em.persist(license);
            else
                em.merge(license);

            tx.commit();
        }
    }

    @Override
    public void delete(PilotLicense license) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            PilotLicense toDelete = em.find(JpaPilotLicense.class, ((JpaPilotLicense) license).getPilotId());
            if (toDelete != null)
                em.remove(toDelete);

            tx.commit();
        }
    }

    @Override
    public PilotLicense get(Integer id) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            return em.find(JpaPilotLicense.class, id);
        }
    }

    @Override
    public Set<PilotLicense> getAll() {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            TypedQuery<JpaPilotLicense> query = em.createQuery("SELECT l FROM JpaPilotLicense l", JpaPilotLicense.class);
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Set<PilotLicense> getByLicenseType(LicenseType licenseType) {
        return Set.of();
    }
}