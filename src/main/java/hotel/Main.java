package hotel;

import java.util.List;

public class Main {
    private static final String CONFIG_FILE = "config.properties";

    public static void main(String[] args) {
        AppConfig config = new AppConfig(CONFIG_FILE);
        JsonStateStorage storage = new JsonStateStorage(config.getStateFilePath());

        // Load saved state
        ApartmentRepository repository = new InMemoryApartmentRepository();
        List<Apartment> saved = storage.load();
        saved.forEach(repository::save);

        ApartmentService service = new ApartmentServiceImpl(repository, config.isStatusChangeEnabled());

        // Save state on exit
        HotelUI ui = new HotelUI(service);
        ui.run();
        storage.save(repository.findAll());
        System.out.println("State saved to " + config.getStateFilePath());
    }
}
