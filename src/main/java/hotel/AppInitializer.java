package hotel;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

@WebListener
public class AppInitializer implements ServletContextListener {

    private static final String CONFIG_FILE = "config.properties";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AppConfig config = new AppConfig(CONFIG_FILE);
        JsonStateStorage storage = new JsonStateStorage(config.getStateFilePath());

        ApartmentRepository repository = new InMemoryApartmentRepository();
        List<Apartment> saved = storage.load();
        saved.forEach(repository::save);

        ApartmentService service = new ApartmentServiceImpl(repository, config.isStatusChangeEnabled());
        ServiceLocator.init(service, storage);

        System.out.println("Hotel app started. State loaded from " + config.getStateFilePath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ApartmentService service = ServiceLocator.getService();
        JsonStateStorage storage = ServiceLocator.getStorage();
        if (service != null && storage != null) {
            storage.save(service.list(Integer.MAX_VALUE, "id"));
            System.out.println("State saved.");
        }
    }
}