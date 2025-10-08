package nl.hva.election_backend.model;

public class ResultDTO {
    private final String partyId;
    private final String candidateId;
    private final int votes;
    private final String regionType;
    private final String regionId;

    public ResultDTO(String partyId, String candidateId, int votes, String regionType, String regionId) {
        this.partyId = partyId;
        this.candidateId = candidateId;
        this.votes = votes;
        this.regionType = regionType;
        this.regionId = regionId;
    }

    public String getPartyId() { return partyId; }
    public String getCandidateId() { return candidateId; }
    public int getVotes() { return votes; }
    public String getRegionType() { return regionType; }
    public String getRegionId() { return regionId; }

    @Override
    public String toString() {
        return String.format("ResultDTO{partyId='%s', candidateId='%s', votes=%d, regionType='%s', regionId='%s'}",
                partyId, candidateId, votes, regionType, regionId);
    }
}
