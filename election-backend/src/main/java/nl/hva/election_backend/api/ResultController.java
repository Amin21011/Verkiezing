package nl.hva.election_backend.api;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/electionresults")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/parties/top")
    public List<Party> getTopParties(
            @RequestParam(defaultValue = "5") int limit
    ) {
        return resultService.getTopParties(limit);
    }

    @GetMapping("/candidates/top")
    public List<Candidate> getTopCandidatesByParty(
            @RequestParam(required = false) String partyId, @RequestParam(defaultValue = "5") int limit
    ) {
        return resultService.getTopCandidatesByParty(partyId, limit);
    }

    @GetMapping("/parties/{id}")
    public Optional<Party> getPartyById(@PathVariable String id) {
        return resultService.getPartyById(id);
    }

}
