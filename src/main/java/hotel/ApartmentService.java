package hotel;

import java.util.List;

public interface ApartmentService {
    Apartment register(int id, double price);
    Apartment reserve(int id, String clientName);
    Apartment release(int id);
    List<Apartment> list(int size, String sortBy);
}
