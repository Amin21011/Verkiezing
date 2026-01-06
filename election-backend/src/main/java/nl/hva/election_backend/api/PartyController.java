package nl.hva.election_backend.api;
import nl.hva.election_backend.dto.model.PartyDTO;
import nl.hva.election_backend.service.PartyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PartyController {
    private final PartyService partyService;
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping("/parties/random")
    public List<PartyDTO> getAllPartiesRandomized() {
        return partyService.getAllParties().stream()
                .map(p -> new PartyDTO(
                        p.getId(),
                        p.getName(),
                        p.getVoteCount()
                ))
                .toList();
    }
}
