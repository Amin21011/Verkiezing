package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Poll;
import nl.hva.election_backend.repository.PollRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
public class PollService {

    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    // Poll aanmaken
    public Poll createPoll(String question, List<String> options) {
        Poll poll = new Poll(question, options);
        return pollRepository.save(poll);
    }

    // Stemmen op poll
    public void voteOnPoll(Long pollId, int optionIndex) {
        Poll poll = pollRepository.findById(pollId).orElse(null);
        if (poll != null && optionIndex >= 0 && optionIndex < poll.getVotes().size()) {
            List<Integer> votes = new ArrayList<>(poll.getVotes());
            votes.set(optionIndex, votes.get(optionIndex) + 1);
            poll.setVotes(votes);
            pollRepository.save(poll);
        }
    }

    public void resetVote(Long pollId, int optionIndex) {
        Poll poll = pollRepository.findById(pollId).orElse(null);
        if (poll != null && optionIndex >= 0 && optionIndex < poll.getVotes().size()) {
            List<Integer> votes = new ArrayList<>(poll.getVotes());
            votes.set(optionIndex, Math.max(votes.get(optionIndex) - 1, 0));
            poll.setVotes(votes);
            pollRepository.save(poll);
        }
    }

    // Poll verwijderen
    public void deletePoll(Long pollId) {
        pollRepository.deleteById(pollId);
    }

    // Poll updaten (stemmen behouden voor bestaande opties)
    public Poll updatePoll(Long pollId, String question, List<String> options) {
        Poll poll = pollRepository.findById(pollId).orElseThrow();

        List<Integer> oldVotes = poll.getVotes();
        List<String> oldOptions = poll.getOptions();
        List<Integer> newVotes = new ArrayList<>();

        for (String option : options) {
            int index = oldOptions.indexOf(option);
            newVotes.add(index != -1 ? oldVotes.get(index) : 0);
        }

        poll.setQuestion(question);
        poll.setOptions(options);
        poll.setVotes(newVotes);

        return pollRepository.save(poll);
    }

    // Alle polls ophalen
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
