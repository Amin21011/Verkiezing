package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
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

        String partyName = repository.getPartyName(partyId);
        if (partyName == null || partyName.isBlank()) {
            partyName = election.findPartyById(partyId)
                    .map(Party::getName)
                    .orElse("(Onbekende partij)");
        }

        Result result = new Result(
                partyId,
                partyName,
                null,
                votes,
                currentRegionType,
                currentRegionId
        );
        repository.addResult(result);

        System.out.printf(
                "PartyVotes -> PartyId=%s (%s), Votes=%d, Region=%s%n",
                partyId, partyName, votes, currentRegionType
        );
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> electionData) {
        String xmlPartyId = electionData.get("AffiliationIdentifier-Id");
        String candidateId = electionData.get("CandidateIdentifier-Id");
        String shortCode = electionData.get("CandidateIdentifier-ShortCode");
        int votes = parseVotes(electionData.getOrDefault("ValidVotes", "0"));

        if (votes <= 0) return;

        Candidate candidate = null;

        if (shortCode != null && !shortCode.isBlank()) {
            candidate = election.getCandidateByShortCode(shortCode).orElse(null);
        }
        if (candidate == null && candidateId != null && !candidateId.isBlank()) {
            candidate = election.getCandidateById(candidateId).orElse(null);
        }

        if (candidate == null) {
            System.out.printf("Geen kandidaat gevonden voor Party=%s, ShortCode=%s, Id=%s%n",
                    xmlPartyId, shortCode, candidateId);
            return;
        }

        String effectivePartyId = candidate.getPartyId() != null ? candidate.getPartyId() : xmlPartyId;

        String partyName = election.findPartyById(effectivePartyId)
                .map(Party::getName)
                .orElse("(Onbekende partij)");
        candidate.setPartyName(partyName);

        candidate.setVotes(candidate.getVotes() + votes);

        Result result = new Result(
                effectivePartyId,
                partyName,
                candidate.getId(),
                votes,
                currentRegionType,
                currentRegionId
        );
        repository.addResult(result);

        System.out.printf("CandidateVotes -> %s %s (%s): +%d stemmen%n",
                candidate.getFirstName(),
                candidate.getLastName(),
                candidate.getPartyName(),
                votes);
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {
        int totalVotes = parseVotes(electionData.getOrDefault("TotalVotes", "0"));
        if (totalVotes <= 0) return;

        Result meta = new Result("META", "", null, totalVotes, currentRegionType, currentRegionId);
        repository.addResult(meta);

        System.out.printf("Metadata -> TotalVotes=%d, Region=%s%n", totalVotes, currentRegionType);
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

        Map<String, Integer> votesByCandidate = new HashMap<>();
        Map<String, Integer> votesByParty = new HashMap<>();

        for (Result r : repository.getAll()) {
            if (r.getCandidateId() != null) {
                votesByCandidate.merge(r.getCandidateId(), r.getVotes(), Integer::sum);
            } else if (r.getPartyId() != null) {
                votesByParty.merge(r.getPartyId(), r.getVotes(), Integer::sum);
            }
        }

        votesByCandidate.forEach((id, total) ->
                election.getCandidateById(id).ifPresent(c -> c.setVotes(total))
        );
        votesByParty.forEach((id, total) ->
                election.findPartyById(id).ifPresent(p -> p.setVoteCount(total))
        );

        System.out.println("Flush complete: unieke kandidaat- en partijstemmingen toegepast.");
    }

}
