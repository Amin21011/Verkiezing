package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.service.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
