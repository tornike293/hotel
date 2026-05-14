package hotel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository repository;
    private final boolean statusChangeEnabled;

    public ApartmentServiceImpl(
            ApartmentRepository repository,
            @Value("${hotel.status.change.enabled:true}") boolean statusChangeEnabled) {
        this.repository = repository;
        this.statusChangeEnabled = statusChangeEnabled;
    }

    @Override
    public Apartment register(int id, double price) {
        BigDecimal bdPrice = BigDecimal.valueOf(price);
        if (bdPrice.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Price must be non-negative.");
        if (repository.existsById(id))
            throw new IllegalArgumentException("Apartment " + id + " already exists.");
        return repository.save(new Apartment(id, bdPrice));
    }

    @Override
    public Apartment reserve(int id, String clientName) {
        if (!statusChangeEnabled)
            throw new IllegalStateException("Reservation changes are disabled.");
        Apartment apartment = findOrThrow(id);
        if (apartment.getReservationStatus())
            throw new IllegalStateException("Apartment " + id + " is already reserved.");
        apartment.reserve(clientName);
        return repository.save(apartment);
    }

    @Override
    public Apartment release(int id) {
        if (!statusChangeEnabled)
            throw new IllegalStateException("Reservation changes are disabled.");
        Apartment apartment = findOrThrow(id);
        if (!apartment.getReservationStatus())
            throw new IllegalStateException("Apartment " + id + " is not reserved.");
        apartment.release();
        return repository.save(apartment);
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
            case "price":  return Comparator.comparing(Apartment::getPrice);
            case "status": return Comparator.comparing(a -> Boolean.toString(a.getReservationStatus()));
            case "client": return Comparator.comparing(
                    a -> a.getClientName() != null ? a.getClientName() : "");
            default:       return Comparator.comparingInt(Apartment::getId);
        }
    }
}