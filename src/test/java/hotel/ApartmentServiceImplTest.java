package hotel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApartmentServiceImplTest {

    private ApartmentService service;

    @BeforeEach
    void setUp() {
        service = new ApartmentServiceImpl(new MockApartmentRepository(), true);
    }

    @Test
    void register_success() {
        Apartment a = service.register(1, 100.0);
        assertEquals(1, a.getId());
        assertEquals(0, a.getPrice().compareTo(BigDecimal.valueOf(100.0)));
        assertFalse(a.getReservationStatus());
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
        assertEquals(0, service.register(1, 0.0).getPrice().compareTo(BigDecimal.ZERO));
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
    void list_sortedByPrice() {
        service.register(1, 100.0);
        service.register(2, 50.0);
        service.register(3, 200.0);
        List<Apartment> result = service.list(10, "price");
        assertEquals(0, result.get(0).getPrice().compareTo(BigDecimal.valueOf(50.0)));
        assertEquals(0, result.get(2).getPrice().compareTo(BigDecimal.valueOf(200.0)));
    }

    @Test
    void reserve_whenDisabled_throws() {
        ApartmentService disabled = new ApartmentServiceImpl(new MockApartmentRepository(), false);
        disabled.register(1, 100.0);
        assertThrows(IllegalStateException.class, () -> disabled.reserve(1, "Alice"));
    }
}