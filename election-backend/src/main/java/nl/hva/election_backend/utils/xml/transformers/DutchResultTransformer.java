package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Result;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.VotesTransformer;

public class DutchResultTransformer implements VotesTransformer {

    private final Election election;
    private ResultRepository repository;
    private String currentRegionType;
    private String currentRegionId;

    public DutchResultTransformer(Election election) {
        this.election = election;
    }

    public void setRepository(ResultRepository repository) {
        this.repository = repository;
    }

    public void setRegionContext(String regionType, String regionId) {
        this.currentRegionType = regionType;
        this.currentRegionId = regionId;
    }

    @Override
    public void registerPartyVotes(boolean aggregated, java.util.Map<String, String> electionData) {
        String partyId = electionData.get("AffiliationIdentifier-Id");
        if (partyId == null) return;

        int votes = parseVotes(
                electionData.getOrDefault("PartyValidVotes",
                        electionData.getOrDefault("ValidVotes", "0"))
        );
        if (votes <= 0) return;

        Result result = new Result(partyId, null, votes, currentRegionType, currentRegionId);
        repository.addResult(result);
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, java.util.Map<String, String> electionData) {
        String shortCode = electionData.get("CandidateIdentifier-ShortCode");
        int votes = parseVotes(electionData.getOrDefault("ValidVotes", "0"));
        if (votes <= 0) return;

        Candidate candidate = election.getCandidateByShortCode(shortCode).orElse(null);
        if (candidate == null) {
            return;
        }

        Result result = new Result(candidate.getPartyId(), candidate.getId(), votes, currentRegionType, currentRegionId);
        result.setShortCode(candidate.getShortCode());
        repository.addResult(result);
    }

    @Override
    public void registerMetadata(boolean aggregated, java.util.Map<String, String> electionData) {
        int totalVotes = parseVotes(electionData.getOrDefault("TotalVotes", "0"));
        Result meta = new Result("META", null, totalVotes, currentRegionType, currentRegionId);
        repository.addResult(meta);
    }

    private int parseVotes(String votesStr) {
        try {
            return Integer.parseInt(votesStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
