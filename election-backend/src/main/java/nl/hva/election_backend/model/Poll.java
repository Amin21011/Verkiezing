package nl.hva.election_backend.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ElementCollection
    private List<String> options = new ArrayList<>();

    @ElementCollection
    private List<Integer> votes = new ArrayList<>();

    private boolean active = false;
    public Poll() {}

    public Poll(String question, List<String> options) {
        this.question = question;
        this.options = options;
        this.votes = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            this.votes.add(0);
        }
        this.active = false;
    }

    // Getters & setters
    public Long getId() { return id; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public List<Integer> getVotes() { return votes; }
    public void setVotes(List<Integer> votes) { this.votes = votes; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
