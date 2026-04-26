package hotel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApartmentTest {

    @Test
    void newApartment_isFreeWithNoClient() {
        Apartment a = new Apartment(1, 100.0);
        assertFalse(a.getReservationStatus());
        assertNull(a.getClientName());
    }

    @Test
    void reserve_setsStatusAndClient() {
        Apartment a = new Apartment(1, 100.0);
        a.reserve("Alice");
        assertTrue(a.getReservationStatus());
        assertEquals("Alice", a.getClientName());
    }

    @Test
    void release_clearsStatusAndClient() {
        Apartment a = new Apartment(1, 100.0);
        a.reserve("Alice");
        a.release();
        assertFalse(a.getReservationStatus());
        assertNull(a.getClientName());
    }
}
