package nl.hva.election_backend.api;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Party1;
import nl.hva.election_backend.service.PartyService;
import org.springframework.http.ResponseEntity;
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
    public List<Party> getTopParties(
            @RequestParam(defaultValue = "3") int limit,
            @RequestParam(required = false) Integer year
    ) {
        return partyService.getTopPartiesByYear(year, limit);
    }

    // Willekeurige volgorde
    @GetMapping("/random")
    public List<Party1> getAllPartiesRandomized() {
        return partyService.getAllPartiesRandomized();
    }
}
