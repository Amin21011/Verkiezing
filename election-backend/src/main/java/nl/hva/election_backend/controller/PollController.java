package nl.hva.election_backend.controller;

import nl.hva.election_backend.model.Poll;
import nl.hva.election_backend.service.PollService;

import java.util.List;

public class PollController {
    private final PollService pollService;

    public PollController() {
        this.pollService = new PollService();
    }

    public void createPoll(String question, List<String> options) {
        Poll poll = new Poll(question, options);
        pollService.addPoll(poll);
    }

    public void voteOnPoll(int pollIndex, int optionIndex) {
        Poll poll = pollService.getPollByIndex(pollIndex);
        if (poll != null) {
            poll.vote(optionIndex);
        }
    }

    public void resetVote(int pollIndex, int optionIndex) {
        Poll poll = pollService.getPollByIndex(pollIndex);
        if (poll != null) {
            poll.resetVote(optionIndex);
        }
    }

    public List<Poll> getAllPolls() {
        return pollService.getPolls();
    }

    public void clearAllPolls() {
        pollService.clearPolls();
    }


}
