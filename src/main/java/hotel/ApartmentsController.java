package hotel;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apartments")
public class ApartmentsController {

    private final ApartmentService service;

    public ApartmentsController(ApartmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Apartment> list(
            @RequestParam(defaultValue = "2147483647") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return service.list(size, sortBy);
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
        try {
            int id = ((Number) body.get("id")).intValue();
            double price = ((Number) body.get("price")).doubleValue();
            return ResponseEntity.status(HttpStatus.CREATED).body(service.register(id, price));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}