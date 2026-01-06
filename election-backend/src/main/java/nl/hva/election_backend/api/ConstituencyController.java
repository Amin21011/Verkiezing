package nl.hva.election_backend.api;

import nl.hva.election_backend.model.ConstituencyResult;
import nl.hva.election_backend.service.ConstituencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/constituencies")
public class ConstituencyController {

    private final ConstituencyService constituencyService;

    public ConstituencyController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    @GetMapping("/results/{year}")
    public List<ConstituencyResult> getConstituencyResults(@PathVariable int year) {
        return constituencyService.getConstituencyResults(year);
    }
}
