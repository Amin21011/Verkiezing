package nl.hva.election_backend.model;

public class Result {
    private final String partyId;
    private final String candidateId;
    private int votes;
    private final String regionType;
    private final String regionId;
    private String partyName; // extra veld

    public Result(String partyId, String candidateId, int votes, String regionType, String regionId) {
        this.partyId = partyId;
        this.candidateId = candidateId;
        this.votes = votes;
        this.regionType = regionType;
        this.regionId = regionId;
    }

    public String getPartyId() { return partyId; }
    public String getCandidateId() { return candidateId; }
    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }

    public String getRegionType() { return regionType; }
    public String getRegionId() { return regionId; }

    public String getPartyName() { return partyName; }
    public void setPartyName(String partyName) { this.partyName = partyName; }

    @Override
    public String toString() {
        String type = candidateId == null ? "Party" : "Candidate";
        return String.format("%s | PartyId: %s (%s) | CandidateId: %s | Votes: %d | Region: %s %s",
                type, partyId, partyName, candidateId, votes, regionType, regionId);
    }
}
