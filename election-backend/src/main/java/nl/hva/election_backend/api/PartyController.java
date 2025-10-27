package nl.hva.election_backend.api;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Party1;
import nl.hva.election_backend.service.PartyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/electionresults")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    // Top partijnamen op basis van stemmen
    @GetMapping("/top")
    public List<Party> getTopParties(@RequestParam(defaultValue = "3") int limit) {
        return partyService.getTopParties(limit);
    }

    // Eén specifieke partij
    @GetMapping("/{id}")
    public Party getPartyById(@PathVariable String id) {
        return partyService.getPartyById(id);
    }

    // Willekeurige volgorde
    @GetMapping("/random")
    public List<Party1> getAllPartiesRandomized() {
        return partyService.getAllPartiesRandomized();
    }
}
