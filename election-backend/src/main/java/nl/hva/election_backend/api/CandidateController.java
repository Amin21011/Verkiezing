package nl.hva.election_backend.api;

import nl.hva.election_backend.dto.model.CandidateDTO;
import nl.hva.election_backend.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:5173")
public class CandidateController {

    private final CandidateService candidateService;
    private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        logger.info("Ophalen van alle kandidaten gestart");
        try {
            List<CandidateDTO> candidates = candidateService.getAllCandidates();
            logger.debug("Aantal kandidaten opgehaald: {}", candidates.size());
            return ResponseEntity.ok(candidates);
        } catch (Exception e) {
            logger.error("Fout bij ophalen kandidaten", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/party/{partyId}")
    public ResponseEntity<List<CandidateDTO>> getCandidatesByParty(
            @PathVariable String partyId
    ) {
        return ResponseEntity.ok(candidateService.getCandidatesByParty(partyId));
    }
    @PostMapping("/compare")
    public ResponseEntity<List<CandidateDTO>> compareCandidates(
            @RequestBody List<Map<String, String>> selections
    ) {
        return ResponseEntity.ok(candidateService.compareCandidates(selections));
    }

}
