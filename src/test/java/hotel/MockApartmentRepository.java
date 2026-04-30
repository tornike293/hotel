package hotel;

import java.util.*;

/**
 * A simple hand-written mock — no Mockito needed.
 * Lets us control exactly what the repository returns
 * so service logic is tested in complete isolation.
 */
public class MockApartmentRepository implements ApartmentRepository {
    private final Map<Integer, Apartment> store = new LinkedHashMap<>();

    @Override
    public void save(Apartment apartment) {
        store.put(apartment.getId(), apartment);
    }

    @Override
    public Optional<Apartment> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Apartment> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsById(int id) {
        return store.containsKey(id);
    }
}
