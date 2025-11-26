package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Result;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ResultRepository {

    private final List<Result> results = new ArrayList<>();
    private final Map<String, Party> partiesById = new HashMap<>();

    public void addResult(Result result) {
        if (result != null) {
            results.add(result);
        }
    }

    public List<Result> getAll() {
        return new ArrayList<>(results);
    }

    public void registerParties(List<Party> parties) {
        partiesById.clear();
        if (parties != null) {
            for (Party p : parties) {
                partiesById.put(p.getId(), p);
            }
        }
    }

    public void clearAll() {
        results.clear();
        partiesById.clear();
        System.out.println("ResultRepository geleegd");
    }

    public String getPartyName(String partyId) {
        if (partyId == null) return "Onbekende partij";
        Party party = partiesById.get(partyId);
        return (party != null) ? party.getName() : "Onbekende partij";
    }

    public List<Party> findTopParties(int limit) {
        if (results.isEmpty()) {
            System.out.println("Geen resultaten beschikbaar in ResultRepository.");
            return List.of();
        }

        Map<String, Integer> votesByParty = new HashMap<>();
        for (Result r : results) {
            if (r.getPartyId() == null) continue;
            votesByParty.merge(r.getPartyId(), r.getVotes(), Integer::sum);
        }

        return votesByParty.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    String id = entry.getKey();
                    int votes = entry.getValue();
                    Party base = partiesById.get(id);
                    String name = (base != null) ? base.getName() : "Onbekende partij";
                    String leaderName = (base != null) ? base.getLeaderName() : "";
                    String website = (base != null) ? base.getWebsite() : "";
                    Party p = new Party(
                            id,
                            name,
                            leaderName != null ? leaderName : "",
                            0,
                            website != null ? website : ""
                    );
                    p.setVoteCount(votes);
                    return p;
                })
                .collect(Collectors.toList());
    }

    public void aggregatePartyVotes(Election election) {
        Map<String, Integer> votesByParty = new HashMap<>();
        for (Result r : results) {
            if (r.getPartyId() != null && r.getCandidateId() == null) {
                votesByParty.merge(r.getPartyId(), r.getVotes(), Integer::sum);
            }
        }

        votesByParty.forEach((partyId, totalVotes) ->
                election.findPartyById(partyId)
                        .ifPresent(p -> p.setVoteCount(totalVotes))
        );
    }

    public void aggregateCandidateVotes(Election election) {
        Map<String, Integer> votesByCandidate = new HashMap<>();

        for (Result r : results) {
            if (r.getCandidateId() != null && r.getPartyId() != null) {
                String key = r.getPartyId() + ":" + r.getCandidateId();
                votesByCandidate.merge(key, r.getVotes(), Integer::sum);
            }
        }

        for (Candidate c : election.getCandidates()) {
            String key = c.getPartyId() + ":" + c.getId();
            int totalVotes = votesByCandidate.getOrDefault(key, 0);
            c.setVotes(totalVotes);
        }
    }

    public void setPartyNames(List<Party> parties) {
        registerParties(parties);
    }

    public int getTotalVotesForParty(String partyId) {
        if (partyId == null) return 0;

        return results.stream()
                .filter(r -> partyId.equals(r.getPartyId()))
                .mapToInt(Result::getVotes)
                .sum();
    }

    public Map<String, Integer> getVotesByParty() {
        Map<String, Integer> votes = new HashMap<>();

        for (Party p : partiesById.values()) {
            int total = getTotalVotesForParty(p.getId());
            votes.put(p.getName(), total); // naam als key → mooi voor simulatie + UI
        }

        return votes;
    }
}
