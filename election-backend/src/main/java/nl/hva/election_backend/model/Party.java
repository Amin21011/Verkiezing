package nl.hva.election_backend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "party")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String partyId;
    private  String name;
    private  String leaderName;
    private int voteCount;
    private  String website;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Candidate> candidates = new ArrayList<>();

    public Party() {}

    public Party(String partyId, String name, String leaderName, int voteCount, String website) {
        this.partyId = partyId;
        this.name = name;
        this.leaderName = leaderName;
        this.voteCount = voteCount;
        this.website = website;
    }

    public String getPartyId() { return partyId; }
    public String getName() { return name; }

    public String getLeaderName() { return leaderName; }
    public String getWebsite() { return website; }

    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    public int getVoteCount() { return voteCount; }

    public void addCandidate(Candidate candidate) { candidates.add(candidate); }
    public List<Candidate> getCandidates() { return candidates; }

    @Override
    public String toString() {
        return name + " (leider: " + leaderName + ") - stemmen: " + voteCount + " - site: " + website;
    }
}
