package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.service.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:5173")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @GetMapping("/party/{partyId}")
    public List<Candidate> getCandidatesByParty(@PathVariable String partyId) {
        return candidateService.getCandidatesByParty(partyId);
    }
    @PostMapping("/compare")
    public List<Candidate> compareCandidates(
            @RequestBody List<Map<String, String>> selections
    ) {
        return candidateService.compareCandidates(selections);
    }

}
