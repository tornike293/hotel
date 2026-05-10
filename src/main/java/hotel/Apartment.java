package hotel;

import java.math.BigDecimal;

public class Apartment {
    private final int id;
    private final BigDecimal price;
    private String clientName;
    private boolean reservationStatus;

    public Apartment(int id, BigDecimal price) {
        this.id = id;
        this.price = price;
        this.reservationStatus = false;
        this.clientName = null;
    }

    public int getId() { return id; }
    public BigDecimal getPrice() { return price; }
    public String getClientName() { return clientName; }
    public boolean getReservationStatus() { return reservationStatus; }

    public void reserve(String clientName) {
        this.clientName = clientName;
        this.reservationStatus = true;
    }

    public void release() {
        this.clientName = null;
        this.reservationStatus = false;
    }

    @Override
    public String toString() {
        return String.format("id=%-3d price=%-8s status=%-10s client=%s",
                id, price, reservationStatus ? "RESERVED" : "FREE",
                clientName != null ? clientName : "-");
    }
}