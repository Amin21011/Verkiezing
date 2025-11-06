package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Poll;
import nl.hva.election_backend.repository.PollRepository;

import java.util.List;

public class PollService {
    private final PollRepository pollRepository;
    public PollService() {
        this.pollRepository = new PollRepository();
    }

    /**
     * Maakt een nieuwe poll aan.
     */
    public void createPoll(String question, List<String> options) {
        Poll poll = new Poll(question, options); // Nieuwe poll aanmaken
        pollRepository.addPoll(poll);
    }

    public void voteOnPoll(int pollIndex, int optionIndex) {
        Poll poll = pollRepository.getPollByIndex(pollIndex); // Haalt de juiste poll op
        if (poll != null) {
            poll.vote(optionIndex); // Voegt een stem toe aan de poll
        }
    }


    public void resetVote(int pollIndex, int optionIndex) {
        Poll poll = pollRepository.getPollByIndex(pollIndex); // Haalt de juiste poll op
        if (poll != null) {
            poll.resetVote(optionIndex); // Verwijdert een stem van de optie
        }
    }

    /**
     * Geeft een lijst van alle polls die er zijn.
     */
    public List<Poll> getAllPolls() {
        return pollRepository.getPolls();
    }

    /**
     * Delete alle polls.
     */
    public void clearAllPolls() {
        pollRepository.clearPolls();
    }
}
