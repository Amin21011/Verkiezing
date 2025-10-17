package nl.hva.election_backend.model;

import java.util.ArrayList;
import java.util.List;

public class Party {
    private final String id;
    private final String name;
    private final String leaderName;
    private int voteCount;
    private final String website;
    private final List<Candidate> candidates = new ArrayList<>();

    public Party(String id, String name, String leaderName, int voteCount, String website) {
        this.id = id;
        this.name = name;
        this.leaderName = leaderName;
        this.voteCount = voteCount;
        this.website = website;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    // nieuw:
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
