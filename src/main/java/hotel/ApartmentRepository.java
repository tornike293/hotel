package hotel;

import java.util.List;
import java.util.Optional;

public interface ApartmentRepository {
    void save(Apartment apartment);
    Optional<Apartment> findById(int id);
    List<Apartment> findAll();
    boolean existsById(int id);
    void update(Apartment apartment);
}