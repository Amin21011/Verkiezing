package nl.hva.election_backend.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "parties")
public class Party {
    @Id
    private String id;

    private String name;
    private int voteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id")
    private Election election;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidate> candidates = new ArrayList<>();

    public Party() {}

    public Party(String id, String name, int voteCount) {
        this.id = id;
        this.name = name;
        this.voteCount = voteCount;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getVoteCount() { return voteCount; }
    public List<Candidate> getCandidates() { return candidates; }
    public Election getElection() { return election; }

    public void setId(String id) { this.id = id; }

    public void setVoteCount(int votes) {
        this.voteCount = votes;
    }

    public void addVotes(int votes) { this.voteCount += votes; }

    public void setElection(Election e) {
        this.election = e;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return name.toLowerCase();
    }
}
