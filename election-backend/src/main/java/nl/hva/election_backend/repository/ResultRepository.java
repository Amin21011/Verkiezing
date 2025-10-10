package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Result;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ResultRepository {

    private final List<Result> results = new ArrayList<>();
    private final Map<String, Party> partiesById = new HashMap<>();

    // Overschrijf oude resultaten door nieuwe (vermijdt duplicates bij meerdere parses)
    public void saveAll(List<Result> resultList) {
        results.clear();
        if (resultList != null) {
            results.addAll(resultList);
        }
    }

    public void clearAll() {
        results.clear();
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

    // Vind top partijen op basis van opgetelde stemmen
    public List<Party> findTopParties(int limit) {
        if (results.isEmpty()) return List.of();

        // sommeer stemmen per partyId
        Map<String, Integer> votesByParty = new HashMap<>();
        for (Result r : results) {
            if (r.getPartyId() == null) continue;
            votesByParty.merge(r.getPartyId(), r.getVotes(), Integer::sum);
        }

        return votesByParty.entrySet().stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    String id = entry.getKey();
                    int votes = entry.getValue();
                    Party base = partiesById.get(id);
                    String name = base != null ? base.getName() : "Onbekende partij";
                    String leader = base != null ? base.getLeaderName() : "";
                    String website = base != null ? base.getWebsite() : "";
                    Party p = new Party(id, name, leader, votes, website);
                    p.setVoteCount(votes);
                    return p;
                })
                .collect(Collectors.toList());
    }

    public String getPartyName(String partyId) {
        if (partyId == null) return "Onbekende partij";
        Party party = partiesById.get(partyId);
        return (party != null) ? party.getName() : "Onbekende partij";
    }
}