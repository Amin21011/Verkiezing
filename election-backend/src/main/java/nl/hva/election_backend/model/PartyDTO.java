package nl.hva.election_backend.model;

import java.util.ArrayList;
import java.util.List;

public class PartyDTO {
    private final String id;
    private final String name;
    private final String leaderName;
    private int voteCount;
    private final String website;
    private final List<CandidateDTO> candidates = new ArrayList<>();

    public PartyDTO(String id, String name, String leaderName, int voteCount, String website) {
        this.id = id;
        this.name = name;
        this.leaderName = leaderName;
        this.voteCount = voteCount;
        this.website = website;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
    public int getVoteCount() { return voteCount; }
    public List<CandidateDTO> getCandidates() { return candidates; }
    public void addCandidate(CandidateDTO candidate) { candidates.add(candidate); }

    @Override
    public String toString() {
        return name + " (leider: " + leaderName + ") - stemmen: " + voteCount + " - site: " + website;
    }
}
