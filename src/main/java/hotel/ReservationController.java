package hotel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/apartments/{id}/reserve")
public class ReservationController {

    private final ApartmentService service;

    public ReservationController(ApartmentService service) {
        this.service = service;
    }

    @PostMapping
    public Apartment reserve(@PathVariable int id, @RequestBody Map<String, String> body) {
        return service.reserve(id, body.get("clientName"));
    }

    @DeleteMapping
    public Apartment release(@PathVariable int id) {
        return service.release(id);
    }
}