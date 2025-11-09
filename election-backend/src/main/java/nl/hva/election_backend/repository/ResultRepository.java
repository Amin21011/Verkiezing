package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Candidate;
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

    /**
     * Geeft partijnamen op basis van ID.
     */
    public String getPartyName(String partyId) {
        if (partyId == null) return "Onbekende partij";
        Party party = partiesById.get(partyId);
        return (party != null) ? party.getName() : "Onbekende partij";
    }

    /**
     * Berekent de top partijen op basis van totaal aantal stemmen.
     */
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
                    String leader = (base != null) ? base.getLeaderName() : "";
                    String website = (base != null) ? base.getWebsite() : "";

                    Party p = new Party(id, name, leader, votes, website);
                    p.setVoteCount(votes);
                    return p;
                })
                .collect(Collectors.toList());
    }

    /**
     * Haalt alle kandidaten op en koppelt hun stemmen
     * vanuit de verzamelde Result-data.
     */
    public List<Candidate> getAllCandidates() {
        if (results.isEmpty()) {
            System.out.println("Geen resultaten gevonden — kandidaten krijgen 0 stemmen.");
            return partiesById.values().stream()
                    .flatMap(p -> p.getCandidates().stream())
                    .collect(Collectors.toList());
        }

        Map<String, Integer> votesByCandidate = new HashMap<>();
        for (Result r : results) {
            if (r.getCandidateId() != null) {
                votesByCandidate.merge(r.getCandidateId(), r.getVotes(), Integer::sum);
            }
        }

        List<Candidate> combined = partiesById.values().stream()
                .flatMap(p -> p.getCandidates().stream())
                .peek(c -> {
                    int votes = votesByCandidate.getOrDefault(c.getId(), 0);
                    c.setVotes(votes);
                })
                .collect(Collectors.toList());

        System.out.printf("%d kandidaten opgehaald met stemdata (gemiddeld %d stemmen)%n",
                combined.size(),
                combined.stream().mapToInt(Candidate::getVotes).sum() / Math.max(1, combined.size()));

        return combined;
    }

    public void setPartyNames(List<Party> parties) {
        registerParties(parties);
    }
}
