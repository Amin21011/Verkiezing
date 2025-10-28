package nl.hva.election_backend.controller;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.service.ResultService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/electionresults")
@CrossOrigin(origins = "*") // frontend mag vrij connecten
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    /**
     * /electionresults/parties/top
     * Geeft de top 3 partijen met de meeste stemmen.
     */

    @GetMapping("/parties/top")
    public List<Party> getTopParties(
            @RequestParam(defaultValue = "3") int limit
    ) {
        return resultService.getTopParties(limit);
    }

//    @GetMapping("/{year}/parties")
//    public List<Party> getAllParties(@PathVariable int year) {
//        return resultService.getAllPartiesByYear(year);
//    }

}
