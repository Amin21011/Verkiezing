package nl.hva.election_backend.api;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.service.ResultService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/electionresults")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    // top 3 partijen met aantal stemmen van elke gekozen kandidaat
    @GetMapping("/parties/top")
    public List<Party> getTopParties(
            @RequestParam(defaultValue = "3") int limit
    ) {
        return resultService.getTopParties(limit);
    }

    @GetMapping("/candidates/top")
    public List<Candidate> getTopCandidatesByParty(
            @RequestParam(required = false) String partyId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return resultService.getTopCandidatesByParty(partyId, limit);
    }
}
