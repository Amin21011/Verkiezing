package nl.hva.election_backend.api;

import nl.hva.election_backend.model.ConstituencyVotes;
import nl.hva.election_backend.service.ConstituencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/constituencies")
public class ConstituencyController {

    private final ConstituencyService constituencyService;

    public ConstituencyController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }


    @PostMapping("/import/{year}")
    public void importResults(@PathVariable int year) {
        constituencyService.importConstituencyResults(year);
    }


    @GetMapping("/results/{year}")
    public List<ConstituencyVotes> getResultsByYear(@PathVariable int year) {
        return constituencyService.getVotesByYear(year);
    }
}
