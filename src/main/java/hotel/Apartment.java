package hotel;

public class Apartment {
    private final int id;
    private final double price;
    private String clientName;
    private boolean reservationStatus;

    public Apartment(int id, double price) {
        this.id = id;
        this.price = price;
        reservationStatus = false;
        clientName = null;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getClientName() {
        return clientName;
    }

    public boolean getReservationStatus() {
        return reservationStatus;
    }

    public void reserve(String clientName) {
        this.clientName = clientName;
        reservationStatus = true;
    }

    public void release() {
        this.clientName = null;
        reservationStatus = false;
    }

    @Override
    public String toString() {
        return String.format("id=%-3d price=%-8.2f status=%-10s client=%s",
                id, price, reservationStatus ? "RESERVED" : "FREE",
                clientName != null ? clientName : "-");
    }
}
