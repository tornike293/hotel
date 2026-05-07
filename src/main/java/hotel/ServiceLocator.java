package hotel;

public class ServiceLocator {
    private static ApartmentService service;
    private static DatabaseConfig databaseConfig;

    public static void init(ApartmentService apartmentService, DatabaseConfig dbConfig) {
        service = apartmentService;
        databaseConfig = dbConfig;
    }

    public static ApartmentService getService() { return service; }
    public static DatabaseConfig getDatabaseConfig() { return databaseConfig; }
}