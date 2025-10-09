package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.service.PartyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parties")
@CrossOrigin(origins = "*")
public class PartyController {
    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @GetMapping
    public List<Party> getAllParties() {
        return partyService.getAllPartiesRandomized();
    }

    @GetMapping("/{id}")
    public Party getPartyById(@PathVariable Long id) {
        return partyService.getPartyById(id);
    }
}