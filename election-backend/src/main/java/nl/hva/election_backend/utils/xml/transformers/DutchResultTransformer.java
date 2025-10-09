package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Result;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.VotesTransformer;

import java.util.HashMap;
import java.util.Map;

public class DutchResultTransformer implements VotesTransformer {

    private final Election election;
    private ResultRepository repository;
    private String currentRegionType;
    private String currentRegionId;

    private final Map<String, Result> results = new HashMap<>();

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
    public void registerPartyVotes(boolean aggregated, Map<String, String> electionData) {
        String partyId = electionData.get("AffiliationIdentifier-Id");
        int votes = parseVotes(electionData.getOrDefault("ValidVotes", "0"));

        if (partyId == null) return;

        String key = "P-" + partyId + "-" + currentRegionType + "-" + currentRegionId;

        results.merge(key,
                new Result(partyId, null, votes, currentRegionType, currentRegionId),
                (oldVal, newVal) -> new Result(
                        oldVal.getPartyId(),
                        oldVal.getCandidateId(),
                        oldVal.getVotes() + newVal.getVotes(),
                        oldVal.getRegionType(),
                        oldVal.getRegionId()
                )
        );
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> electionData) {
        String shortCode = electionData.get("CandidateIdentifier-ShortCode");
        int votes = parseVotes(electionData.getOrDefault("ValidVotes", "0"));

        if (shortCode == null) return;

        Candidate candidate = election.getCandidates().stream()
                .filter(c -> shortCode.equals(c.getShortCode()))
                .findFirst()
                .orElse(null);

        if (candidate == null) return;

        String partyId = candidate.getPartyId();
        String key = "C-" + shortCode + "-" + currentRegionType + "-" + currentRegionId;

        results.merge(key,
                new Result(partyId, shortCode, votes, currentRegionType, currentRegionId),
                (oldVal, newVal) -> new Result(
                        oldVal.getPartyId(),
                        oldVal.getCandidateId(),
                        oldVal.getVotes() + newVal.getVotes(),
                        oldVal.getRegionType(),
                        oldVal.getRegionId()
                )
        );
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {
        int totalVotes = parseVotes(electionData.getOrDefault("TotalVotes", "0"));
        results.put("META-" + currentRegionType + "-" + currentRegionId,
                new Result("META", null, totalVotes, currentRegionType, currentRegionId)
        );
    }

    public void flushResults() {
        if (repository != null && !results.isEmpty()) {
            repository.saveAll(results.values().stream().toList());
            results.clear();
        }
    }

    private int parseVotes(String votesStr) {
        try {
            return Integer.parseInt(votesStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
