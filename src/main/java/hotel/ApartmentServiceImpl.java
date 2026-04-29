package hotel;

import java.util.Comparator;
import java.util.List;

public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository repository;

    public ApartmentServiceImpl(ApartmentRepository repository) {
        this.repository = repository;
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
        Apartment apartment = findOrThrow(id);
        if (apartment.getReservationStatus())
            throw new IllegalStateException("Apartment " + id + " is already reserved.");
        apartment.reserve(clientName);
        return apartment;
    }

    @Override
    public Apartment release(int id) {
        Apartment apartment = findOrThrow(id);
        if (!apartment.getReservationStatus())
            throw new IllegalStateException("Apartment " + id + " is not reserved.");
        apartment.release();
        return apartment;
    }

    @Override
    public List<Apartment> list(int size, String sortBy) {
        List<Apartment> all = repository.findAll();
        all.sort(comparatorFor(sortBy));
        return all.subList(0, Math.min(size, all.size()));
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
