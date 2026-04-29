package hotel;

public class Main {
    public static void main(String[] args) {
        ApartmentRepository repository = new InMemoryApartmentRepository();
        ApartmentService service = new ApartmentServiceImpl(repository);
        new HotelUI(service).run();
    }
}
