package hotel;

import java.util.*;

public class InMemoryApartmentRepository implements ApartmentRepository {
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
