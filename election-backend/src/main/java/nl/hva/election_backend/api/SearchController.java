package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.SearchRequest;
import nl.hva.election_backend.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:5173")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public Map<String, Object> search(@RequestParam String name) {
        List<Candidate> candidates = searchService.searchCandidates(name);
        List<Party> parties = searchService.searchParties(name);

        Map<String, Object> results = new HashMap<>();
        results.put("candidates", candidates);
        results.put("parties", parties);

        return results;
    }

}
