package hotel;


public class ServiceLocator {
    private static ApartmentService service;
    private static JsonStateStorage storage;

    public static void init(ApartmentService apartmentService, JsonStateStorage stateStorage) {
        service = apartmentService;
        storage = stateStorage;
    }

    public static ApartmentService getService() {
        return service;
    }

    public static JsonStateStorage getStorage() {
        return storage;
    }
}