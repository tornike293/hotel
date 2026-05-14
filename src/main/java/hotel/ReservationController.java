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
    public ResponseEntity<?> reserve(@PathVariable int id, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(service.reserve(id, body.get("clientName")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> release(@PathVariable int id) {
        try {
            return ResponseEntity.ok(service.release(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }
}