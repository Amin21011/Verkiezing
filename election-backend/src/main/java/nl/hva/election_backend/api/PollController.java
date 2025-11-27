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

    // Stemmen op optie
    @PostMapping("/{pollId}/vote/{optionIndex}")
    public void vote(@PathVariable Long pollId, @PathVariable int optionIndex) {
        pollService.voteOnPoll(pollId, optionIndex);
    }

    // Stem resetten
    @PutMapping("/{pollId}/reset/{optionIndex}")
    public void resetVote(@PathVariable Long pollId, @PathVariable int optionIndex) {
        pollService.resetVote(pollId, optionIndex);
    }

    // Poll aanmaken
    @PostMapping
    public Poll createPoll(@RequestBody Poll poll) {
        return pollService.createPoll(poll.getQuestion(), poll.getOptions());
    }

    // Poll updaten
    @PutMapping("/{pollId}")
    public Poll updatePoll(@PathVariable Long pollId, @RequestBody Poll poll) {
        return pollService.updatePoll(pollId, poll.getQuestion(), poll.getOptions());
    }

    /**
     * Verwijdert alle polls.
     */
    @DeleteMapping("/{pollId}")
    public void deletePoll(@PathVariable Long pollId) {
        pollService.deletePoll(pollId);
    }
}
