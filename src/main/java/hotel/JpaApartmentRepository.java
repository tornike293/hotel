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
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(apartment);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save apartment", e);
        }
    }

    @Override
    public void update(Apartment apartment) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(apartment);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update apartment", e);
        }
    }

    @Override
    public Optional<Apartment> findById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.find(Apartment.class, id));
        }
    }

    @Override
    public List<Apartment> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("FROM Apartment", Apartment.class).getResultList();
        }
    }

    @Override
    public boolean existsById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Apartment.class, id) != null;
        }
    }
}