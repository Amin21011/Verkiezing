package nl.hva.election_backend.model;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private final String id;
    private final String name;
    private final String leaderName;
    private final String website;

    private int voteCount;

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

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }

    public String getLeaderName() { return leaderName; }
    public String getWebsite() { return website; }

    public List<Candidate> getCandidates() { return candidates; }
    public void addCandidate(Candidate c) { candidates.add(c); }
}
