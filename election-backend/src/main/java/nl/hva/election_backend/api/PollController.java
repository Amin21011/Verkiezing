package nl.hva.election_backend.api;

import nl.hva.election_backend.service.PollService;
import nl.hva.election_backend.model.Poll;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:5173")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    /**
     * Haalt alle polls op.
     */
    @GetMapping
    public List<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }

    @PostMapping("/{pollId}/vote/{optionIndex}")
    public void vote(@PathVariable Long pollId, @PathVariable int optionIndex) {
        pollService.voteOnPoll(pollId, optionIndex);
    }

    @PutMapping("/{pollId}/reset/{optionIndex}")
    public void resetVote(@PathVariable Long pollId, @PathVariable int optionIndex) {
        pollService.resetVote(pollId, optionIndex);
    }

    /**
     * Verwijdert alle polls.
     */
    @DeleteMapping("/clear")
    public void clearPolls() {
        pollService.clearAllPolls();
    }
}
