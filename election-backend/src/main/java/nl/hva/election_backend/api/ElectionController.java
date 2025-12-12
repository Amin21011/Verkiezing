package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.service.DutchElectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elections")
public class ElectionController {
    private final DutchElectionService electionService;

    public ElectionController(DutchElectionService electionService) {
        this.electionService = electionService;
    }

    @PostMapping("/{electionId}")
    public Election readResults(@PathVariable String electionId, @RequestParam(required = false) String folderName) {
        if (folderName == null) {
            return electionService.readResults(electionId, electionId);
        } else return electionService.readResults(electionId, folderName);
    }
}
