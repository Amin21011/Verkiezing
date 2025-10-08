package nl.hva.election_backend.api;

import nl.hva.election_backend.controller.PollController;
import nl.hva.election_backend.model.Poll;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:5173")
public class PollApi {
    private final PollController pollController;

    public PollApi() {
        this.pollController = new PollController();

        pollController.createPoll("Wie is de betere partij?", Arrays.asList("VVD", "D66", "CDA"));
    }

    @GetMapping
    public List<Poll> getAllPolls() {
        return pollController.getAllPolls();
    }

    @PostMapping("/{pollIndex}/vote/{optionIndex}")
    public void vote(@PathVariable int pollIndex, @PathVariable int optionIndex) {
        pollController.voteOnPoll(pollIndex, optionIndex);
    }

    @PutMapping("/{pollIndex}/reset/{optionIndex}")
    public void resetVote(@PathVariable int pollIndex, @PathVariable int optionIndex) {
        pollController.resetVote(pollIndex, optionIndex);
    }
    @DeleteMapping("/clear")
    public void clearPolls() {
        pollController.clearAllPolls();
    }


}
