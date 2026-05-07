package hotel;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {
    private static final String CONFIG_FILE = "config.properties";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConfig dbConfig = new DatabaseConfig(CONFIG_FILE);
        ApartmentRepository repository = new DatabaseApartmentRepository(dbConfig.getDataSource());

        boolean statusChangeEnabled = loadStatusChangeEnabled();
        ApartmentService service = new ApartmentServiceImpl(repository, statusChangeEnabled);
        ServiceLocator.init(service, dbConfig);

        System.out.println("Hotel app started. Connected to database.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseConfig dbConfig = ServiceLocator.getDatabaseConfig();
        if (dbConfig != null) {
            dbConfig.close();
            System.out.println("Database connection pool closed.");
        }
    }

    private boolean loadStatusChangeEnabled() {
        try (java.io.InputStream in = new java.io.FileInputStream(CONFIG_FILE)) {
            java.util.Properties props = new java.util.Properties();
            props.load(in);
            return Boolean.parseBoolean(props.getProperty("hotel.status.change.enabled", "true"));
        } catch (Exception e) {
            return true;
        }
    }
}