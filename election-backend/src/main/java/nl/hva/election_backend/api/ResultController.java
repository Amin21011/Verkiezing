package nl.hva.election_backend.api;
import nl.hva.election_backend.dto.model.CandidateDTO;
import nl.hva.election_backend.dto.model.PartyDTO;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.service.ResultService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResultController {
    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/parties/top")
    public List<PartyDTO> getTopParties(@RequestParam(defaultValue = "5") int limit) {
        return resultService.getTopParties(limit).stream()
                .map(p -> new PartyDTO(
                        p.getId(),
                        p.getName(),
                        p.getVoteCount()
                ))
                .toList();
    }

    @GetMapping("/results/candidates/top")
    public List<CandidateDTO> getTopCandidatesByParty(
            @RequestParam String partyId,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return resultService.getTopCandidatesByParty(partyId, limit).stream()
                .map(c -> new CandidateDTO(
                        c.getId(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getParty().getId(),
                        c.getParty().getName(),
                        c.getGender(),
                        c.getVotes()
                ))
                .toList();
    }

    @GetMapping("/parties/{id}")
    public PartyDTO getPartyById(@PathVariable String id) {
        Party p = resultService.getPartyById(id);
        if (p == null) return null;
        return new PartyDTO(
                p.getId(),
                p.getName(),
                p.getVoteCount()
        );
    }
}
