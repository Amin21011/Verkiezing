package nl.hva.election_backend.model;

import java.util.List;

public class Poll {
    private String question;
    private List<String> options;
    private int[] votes;

    public Poll(String question, List<String> options) {
        this.question = question;
        this.options = options;
        this.votes = new int[options.size()];
    }

    // Pollvraag teruggeven
    public String getQuestion() {
        return question;
    }

    // Lijst met antwoordopties teruggeven
    public List<String> getOptions() {
        return options;
    }

    // Aantal stemmen per optie teruggeven
    public int[] getVotes() {
        return votes;
    }

    public void vote(int optionIndex) {
        if (optionIndex >= 0 && optionIndex < votes.length) {
            votes[optionIndex]++;
        }
    }

    public void resetVote(int optionIndex) {
        if (optionIndex >= 0 && optionIndex < votes.length && votes[optionIndex] > 0) {
            votes[optionIndex]--;
        }
    }
}
