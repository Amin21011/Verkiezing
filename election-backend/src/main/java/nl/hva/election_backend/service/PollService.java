package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Poll;
import nl.hva.election_backend.repository.PollRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class PollService {

    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll createPoll(String question, List<String> options) {
        Poll poll = new Poll(question, options);
        return pollRepository.save(poll);
    }

    public void voteOnPoll(Long pollId, int optionIndex) {
        Poll poll = pollRepository.findById(pollId).orElse(null);
        if (poll != null && optionIndex >= 0 && optionIndex < poll.getVotes().size()) {
            List<Integer> votes = poll.getVotes();
            votes.set(optionIndex, votes.get(optionIndex) + 1);
            poll.setVotes(votes);
            pollRepository.save(poll);
        }
    }

    public void resetVote(Long pollId, int optionIndex) {
        Poll poll = pollRepository.findById(pollId).orElse(null);
        if (poll != null && optionIndex >= 0 && optionIndex < poll.getVotes().size()) {
            List<Integer> votes = poll.getVotes();
            votes.set(optionIndex, Math.max(votes.get(optionIndex) - 1, 0));
            poll.setVotes(votes);
            pollRepository.save(poll);
        }
    }

    /**
     * Geeft een lijst van alle polls die er zijn.
     */
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    /**
     * Delete alle polls.
     */
    public void clearAllPolls() {
        pollRepository.deleteAll();
    }

    @PostConstruct
    public void init() {
        if (pollRepository.count() == 0) {
            createPoll("Wie is de betere partij?", Arrays.asList("VVD", "D66", "CDA"));
            System.out.println("✅ Initiele poll toegevoegd");
        }
    }
}
