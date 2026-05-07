package hotel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository repository;
    private final boolean statusChangeEnabled;

    public ApartmentServiceImpl(ApartmentRepository repository, boolean statusChangeEnabled) {
        this.repository = repository;
        this.statusChangeEnabled = statusChangeEnabled;
    }

    @Override
    public Apartment register(int id, double price) {
        if (price < 0)
            throw new IllegalArgumentException("Price must be non-negative.");
        if (repository.existsById(id))
            throw new IllegalArgumentException("Apartment " + id + " already exists.");
        Apartment apartment = new Apartment(id, price);
        repository.save(apartment);
        return apartment;
    }

    @Override
    public Apartment reserve(int id, String clientName) {
        if (!statusChangeEnabled)
            throw new IllegalStateException("Reservation changes are disabled.");
        Apartment apartment = findOrThrow(id);
        if (apartment.getReservationStatus())
            throw new IllegalStateException("Apartment " + id + " is already reserved.");
        apartment.reserve(clientName);
        repository.update(apartment);
        return apartment;
    }

    @Override
    public Apartment release(int id) {
        if (!statusChangeEnabled)
            throw new IllegalStateException("Reservation changes are disabled.");
        Apartment apartment = findOrThrow(id);
        if (!apartment.getReservationStatus())
            throw new IllegalStateException("Apartment " + id + " is not reserved.");
        apartment.release();
        repository.update(apartment);
        return apartment;
    }

    @Override
    public List<Apartment> list(int size, String sortBy) {
        return repository.findAll().stream()
                .sorted(comparatorFor(sortBy))
                .limit(size)
                .collect(Collectors.toList());
    }

    private Apartment findOrThrow(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apartment " + id + " not found."));
    }

    private Comparator<Apartment> comparatorFor(String sortBy) {
        switch (sortBy) {
            case "price":  return Comparator.comparingDouble(Apartment::getPrice);
            case "status": return Comparator.comparing(a -> Boolean.toString(a.getReservationStatus()));
            case "client": return Comparator.comparing(
                    a -> a.getClientName() != null ? a.getClientName() : "");
            default:       return Comparator.comparingInt(Apartment::getId);
        }
    }
}