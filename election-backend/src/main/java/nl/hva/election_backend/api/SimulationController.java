package nl.hva.election_backend.api;

import nl.hva.election_backend.dto.SeatSimulationRequest;
import nl.hva.election_backend.service.SeatSimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SeatSimulationService service;

    public SimulationController(SeatSimulationService service) {
        this.service = service;
    }

    @PostMapping("/seats")
    public ResponseEntity<?> simulate(@RequestBody SeatSimulationRequest req) {

        if (req.turnout() < 0 || req.turnout() > 100)
            return ResponseEntity.badRequest().body("Turnout must be 0-100");

        if (req.threshold() < 0 || req.threshold() > 5)
            return ResponseEntity.badRequest().body("Threshold too high");

        return ResponseEntity.ok(service.simulate(req));
    }
}
