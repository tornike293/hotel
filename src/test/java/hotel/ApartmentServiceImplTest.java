package hotel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApartmentServiceImplTest {

    private ApartmentService service;

    @BeforeEach
    void setUp() {
        service = new ApartmentServiceImpl(new MockApartmentRepository());
    }

    @Test
    void register_success() {
        Apartment a = service.register(1, 100.0);
        assertEquals(1, a.getId());
        assertEquals(100.0, a.getPrice());
        assertFalse(a.getReservationStatus());
        assertNull(a.getClientName());
    }

    @Test
    void register_duplicateId_throws() {
        service.register(1, 100.0);
        assertThrows(IllegalArgumentException.class, () -> service.register(1, 200.0));
    }

    @Test
    void register_negativePrice_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.register(1, -1.0));
    }

    @Test
    void register_zeroPriceAllowed() {
        Apartment a = service.register(1, 0.0);
        assertEquals(0.0, a.getPrice());
    }

    @Test
    void reserve_success() {
        service.register(1, 100.0);
        Apartment a = service.reserve(1, "Alice");
        assertTrue(a.getReservationStatus());
        assertEquals("Alice", a.getClientName());
    }

    @Test
    void reserve_alreadyReserved_throws() {
        service.register(1, 100.0);
        service.reserve(1, "Alice");
        assertThrows(IllegalStateException.class, () -> service.reserve(1, "Bob"));
    }

    @Test
    void reserve_notFound_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.reserve(99, "Alice"));
    }

    @Test
    void release_success() {
        service.register(1, 100.0);
        service.reserve(1, "Alice");
        Apartment a = service.release(1);
        assertFalse(a.getReservationStatus());
        assertNull(a.getClientName());
    }

    @Test
    void release_notReserved_throws() {
        service.register(1, 100.0);
        assertThrows(IllegalStateException.class, () -> service.release(1));
    }

    @Test
    void release_notFound_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.release(99));
    }

    @Test
    void list_returnsAllWhenSizeLarge() {
        service.register(1, 100.0);
        service.register(2, 200.0);
        service.register(3, 50.0);
        assertEquals(3, service.list(10, "id").size());
    }

    @Test
    void list_respectsSize() {
        service.register(1, 100.0);
        service.register(2, 200.0);
        service.register(3, 50.0);
        assertEquals(2, service.list(2, "id").size());
    }

    @Test
    void list_sortedByPrice() {
        service.register(1, 100.0);
        service.register(2, 50.0);
        service.register(3, 200.0);
        List<Apartment> result = service.list(10, "price");
        assertEquals(50.0, result.get(0).getPrice());
        assertEquals(100.0, result.get(1).getPrice());
        assertEquals(200.0, result.get(2).getPrice());
    }

    @Test
    void list_sortedById() {
        service.register(3, 100.0);
        service.register(1, 200.0);
        service.register(2, 50.0);
        List<Apartment> result = service.list(10, "id");
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(3, result.get(2).getId());
    }

    @Test
    void list_sortedByStatus() {
        service.register(1, 100.0);
        service.register(2, 200.0);
        service.reserve(2, "Alice");
        List<Apartment> result = service.list(10, "status");
        assertFalse(result.get(0).getReservationStatus());
        assertTrue(result.get(1).getReservationStatus());
    }

    @Test
    void list_sortedByClient() {
        service.register(1, 100.0);
        service.register(2, 200.0);
        service.register(3, 300.0);
        service.reserve(1, "Charlie");
        service.reserve(2, "Alice");
        List<Apartment> result = service.list(10, "client");
        assertNull(result.get(0).getClientName());
        assertEquals("Alice", result.get(1).getClientName());
        assertEquals("Charlie", result.get(2).getClientName());
    }

    @Test
    void list_emptyRepository() {
        assertTrue(service.list(10, "id").isEmpty());
    }
}
