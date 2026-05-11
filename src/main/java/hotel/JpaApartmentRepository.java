package hotel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

public class JpaApartmentRepository implements ApartmentRepository {
    private final EntityManagerFactory emf;

    public JpaApartmentRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Apartment apartment) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(apartment);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to save apartment", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Apartment apartment) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(apartment);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to update apartment", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Apartment> findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Apartment.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Apartment> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM Apartment", Apartment.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Apartment.class, id) != null;
        } finally {
            em.close();
        }
    }
}