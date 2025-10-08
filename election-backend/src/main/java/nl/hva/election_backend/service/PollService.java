package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Poll;
import java.util.ArrayList;
import java.util.List;

public class PollService {
    private List<Poll> polls = new ArrayList<>();

    // Toevoeging van een nieuwe poll aan de lijst
    public void addPoll(Poll poll) {
        polls.add(poll);
    }

    // Het teruggeven van de lijst met alle polls
    public List<Poll> getPolls() {
        return polls;
    }

    // Het zoeken van een poll op basis van de index in de lijst
    public Poll getPollByIndex(int index) {
        if (index >= 0 && index < polls.size()) {
            return polls.get(index);
        }
        return null;
    }

    // Verwijdert alle polls uit de lijst
    public void clearPolls() {
        polls.clear();
    }


}
