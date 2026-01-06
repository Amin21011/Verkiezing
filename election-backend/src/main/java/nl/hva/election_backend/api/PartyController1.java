package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Party1;
import nl.hva.election_backend.service.PartyService1;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parties")
public class PartyController1 {
    private final PartyService1 partyService1;

    public PartyController1(PartyService1 partyService1) {
        this.partyService1 = partyService1;
    }

    @GetMapping
    public List<Party1> getAllParties() {
        return partyService1.getAllPartiesRandomized();
    }

//    @GetMapping("/{id}")
//    public Party1 getPartyById(@PathVariable Long id) {
//        return partyService1.getPartyById(id);
//    }
}