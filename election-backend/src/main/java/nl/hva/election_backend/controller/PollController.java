package nl.hva.election_backend.controller;

import nl.hva.election_backend.model.Poll;
import nl.hva.election_backend.service.PollService;

import java.util.List;

public class PollController {
    private final PollService pollService;
    public PollController() {
        this.pollService = new PollService();
    }

    /**
     * Maakt een nieuwe poll aan.
     */
    public void createPoll(String question, List<String> options) {
        Poll poll = new Poll(question, options); // Nieuwe poll aanmaken
        pollService.addPoll(poll);
    }

    public void voteOnPoll(int pollIndex, int optionIndex) {
        Poll poll = pollService.getPollByIndex(pollIndex); // Haalt de juiste poll op
        if (poll != null) {
            poll.vote(optionIndex); // Voegt een stem toe aan de poll
        }
    }


    public void resetVote(int pollIndex, int optionIndex) {
        Poll poll = pollService.getPollByIndex(pollIndex); // Haalt de juiste poll op
        if (poll != null) {
            poll.resetVote(optionIndex); // Verwijdert een stem van de optie
        }
    }

    /**
     * Geeft een lijst van alle polls die er zijn.
     */
    public List<Poll> getAllPolls() {
        return pollService.getPolls();
    }

    /**
     * Delete alle polls.
     */
    public void clearAllPolls() {
        pollService.clearPolls();
    }
}
