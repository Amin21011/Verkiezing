package nl.hva.election_backend.model;

public class PartyDTO {
    private final String id;
    private final String name;
    private final String leaderName;
    private int voteCount;
    private final String website;

    public PartyDTO(String id, String name, String leaderName, int voteCount, String website) {
        this.id = id;
        this.name = name;
        this.leaderName = leaderName;
        this.voteCount = voteCount;
        this.website = website;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLeaderName() { return leaderName; }
    public int getVoteCount() { return voteCount; }
    public String getWebsite() { return website; }

    // Correcte implementatie van setVoteCount
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return name + " (leider: " + leaderName + ") - stemmen: " + voteCount + " - site: " + website;
    }
}
