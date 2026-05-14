package hotel;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;

public class MockApartmentRepository implements ApartmentRepository {
    private final Map<Integer, Apartment> store = new LinkedHashMap<>();

    @Override
    public Apartment save(Apartment apartment) {
        store.put(apartment.getId(), apartment);
        return apartment;
    }

    @Override
    public Optional<Apartment> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean existsById(Integer id) {
        return store.containsKey(id);
    }

    @Override
    public List<Apartment> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override public List<Apartment> findAll(Sort sort) { return findAll(); }
    @Override public List<Apartment> findAllById(Iterable<Integer> ids) { return List.of(); }
    @Override public long count() { return store.size(); }
    @Override public void deleteById(Integer id) { store.remove(id); }
    @Override public void delete(Apartment entity) { store.remove(entity.getId()); }
    @Override public void deleteAllById(Iterable<? extends Integer> ids) {}
    @Override public void deleteAll(Iterable<? extends Apartment> entities) {}
    @Override public void deleteAll() { store.clear(); }
    @Override public <S extends Apartment> List<S> saveAll(Iterable<S> entities) { return List.of(); }
    @Override public void flush() {}
    @Override public <S extends Apartment> S saveAndFlush(S entity) { return (S) save(entity); }
    @Override public <S extends Apartment> List<S> saveAllAndFlush(Iterable<S> entities) { return List.of(); }
    @Override public void deleteAllInBatch(Iterable<Apartment> entities) {}
    @Override public void deleteAllByIdInBatch(Iterable<Integer> ids) {}
    @Override public void deleteAllInBatch() {}
    @Override public Apartment getOne(Integer id) { return store.get(id); }
    @Override public Apartment getById(Integer id) { return store.get(id); }
    @Override public Apartment getReferenceById(Integer id) { return store.get(id); }
    @Override public <S extends Apartment> Optional<S> findOne(Example<S> example) { return Optional.empty(); }
    @Override public <S extends Apartment> List<S> findAll(Example<S> example) { return List.of(); }
    @Override public <S extends Apartment> List<S> findAll(Example<S> example, Sort sort) { return List.of(); }
    @Override public <S extends Apartment> Page<S> findAll(Example<S> example, Pageable pageable) { return Page.empty(); }
    @Override public <S extends Apartment> long count(Example<S> example) { return 0; }
    @Override public <S extends Apartment> boolean exists(Example<S> example) { return false; }
    @Override public <S extends Apartment, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { return null; }
    @Override public Page<Apartment> findAll(Pageable pageable) { return Page.empty(); }
}