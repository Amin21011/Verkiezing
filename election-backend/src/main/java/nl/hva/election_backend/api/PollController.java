package nl.hva.election_backend.api;

import nl.hva.election_backend.service.PollService;
import nl.hva.election_backend.model.Poll;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/polls")
@CrossOrigin(origins = "http://localhost:5173")
public class PollController {
    private final PollService pollService;

    public PollController() {
        this.pollService = new PollService();

        pollService.createPoll("Wie is de betere partij?", Arrays.asList("VVD", "D66", "CDA"));
    }

    /**
     * Haalt alle polls op.
     */
    @GetMapping
    public List<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }

    /**
     * Het toevoegen van een stem aan een poll.
     */
    @PostMapping("/{pollIndex}/vote/{optionIndex}")
    public void vote(@PathVariable int pollIndex, @PathVariable int optionIndex) {
        pollService.voteOnPoll(pollIndex, optionIndex);
    }

    /**
     * Haalt een stem weg.
     */
    @PutMapping("/{pollIndex}/reset/{optionIndex}")
    public void resetVote(@PathVariable int pollIndex, @PathVariable int optionIndex) {
        pollService.resetVote(pollIndex, optionIndex);
    }

    /**
     * Verwijdert alle polls.
     */
    @DeleteMapping("/clear")
    public void clearPolls() {
        pollService.clearAllPolls();
    }


}
