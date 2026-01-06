package nl.hva.election_backend.api;

import nl.hva.election_backend.model.ProvinceCompareRequest;
import nl.hva.election_backend.model.ProvinceResult;
import nl.hva.election_backend.service.ProvinceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@CrossOrigin(origins = "http://localhost:5173")
public class ProvinceController {
    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping("/results/{year}")
    public ResponseEntity<?> getProvinceResults(@PathVariable int year) {
        System.out.println("GET /api/provinces/results/" + year);

        try {
            List<ProvinceResult> results = provinceService.getProvincieResultaten(year);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            System.out.println("Fout bij ophalen provincie resultaten: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/compare")
    public ResponseEntity<?> compareProvinces(@RequestBody ProvinceCompareRequest request) {
        System.out.println("POST /api/provinces/compare");

        try {
            List<ProvinceResult> results =
                    provinceService.compareProvinces(request.getYear(), request.getProvinces());
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            System.out.println("Fout bij vergelijken provincies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
