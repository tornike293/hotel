package hotel;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "reserved", nullable = false)
    private boolean reservationStatus;

    protected Apartment() {}

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
}