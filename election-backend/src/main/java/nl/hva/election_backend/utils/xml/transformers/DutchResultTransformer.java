package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Result;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.VotesTransformer;
import java.util.Map;

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
    public void registerPartyVotes(boolean aggregated, Map<String, String> electionData) {
        String partyId = electionData.get("AffiliationIdentifier-Id");
        if (partyId == null || partyId.isBlank()) return;

        int votes = parseVotes(
                electionData.getOrDefault("PartyValidVotes",
                        electionData.getOrDefault("ValidVotes", "0"))
        );
        if (votes <= 0) return;

        String partyName = election.findPartyById(partyId)
                .map(Party::getName)
                .orElse(repository.getPartyName(partyId));

        if (partyName == null || partyName.isBlank()) partyName = "(Onbekende partij)";

        Result result = new Result(
                partyId,
                partyName,
                null,
                votes,
                currentRegionType,
                currentRegionId
        );
        repository.addResult(result);

        System.out.printf("PartyVotes -> PartyId=%s (%s), Votes=%d, Region=%s %s%n",
                partyId, partyName, votes, currentRegionType, currentRegionId);
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> electionData) {
        String partyId = electionData.get("AffiliationIdentifier-Id");
        String candidateId = electionData.get("CandidateIdentifier-Id");
        String shortCode = electionData.get("CandidateIdentifier-ShortCode");
        int votes = parseVotes(electionData.getOrDefault("ValidVotes", "0"));
        if (votes <= 0) return;

        if (candidateId == null || partyId == null) return;

        Candidate candidate = election.getCandidateById(candidateId).orElse(null);
        if (candidate == null) return;

        String partyName = election.findPartyById(partyId)
                .map(Party::getName)
                .orElse(repository.getPartyName(partyId));

        Result result = new Result(
                partyId,
                partyName,
                candidateId,
                votes,
                currentRegionType,
                currentRegionId
        );
        result.setShortCode(shortCode);
        repository.addResult(result);
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {
        int totalVotes = parseVotes(electionData.getOrDefault("TotalVotes", "0"));
        if (totalVotes <= 0) return;

        Result meta = new Result("META", "", null, totalVotes, currentRegionType, currentRegionId);
        repository.addResult(meta);
    }

    private int parseVotes(String votesStr) {
        try {
            return Integer.parseInt(votesStr.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public void flushResults() {
        if (repository == null) return;

        System.out.println("Aggregating results into Election model...");

        election.getCandidates().forEach(c -> c.setVotes(0));
        election.getParties().forEach(p -> p.setVoteCount(0));
        repository.aggregatePartyVotes(election);
        repository.aggregateCandidateVotes(election);

        int totalVotes = election.getCandidates().stream()
                .mapToInt(Candidate::getVotes).sum();

        System.out.printf("Flush voltooid: %d kandidaten, totaal %d stemmen.%n",
                election.getCandidates().size(), totalVotes);

        election.getParties().forEach(p ->
                System.out.printf("→ %s (%s): %d stemmen%n",
                        p.getName(), p.getId(), p.getVoteCount()));
    }

}
