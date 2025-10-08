package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Poll;
import java.util.ArrayList;
import java.util.List;

public class PollService {
    private List<Poll> polls = new ArrayList<>();

    public void addPoll(Poll poll) {
        polls.add(poll);
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public Poll getPollByIndex(int index) {
        if (index >= 0 && index < polls.size()) {
            return polls.get(index);
        }
        return null;
    }

    public void clearPolls() {
        polls.clear();
    }


}
