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

    public void saveAll(List<Result> resultList) {
        if (resultList != null) {
            results.addAll(resultList);
        }
    }

    public List<Result> getAll() {
        return new ArrayList<>(results);
    }

    /** Registreer bekende partijen (id → Party-object) */
    public void registerParties(List<Party> parties) {
        partiesById.clear();
        for (Party p : parties) {
            partiesById.put(p.getId(), p);
        }
    }

    /** 🔄 Backwards compatibility voor oude code die setPartyNames() gebruikt */
    public void setPartyNames(List<Party> parties) {
        registerParties(parties);
    }

    /** Alle resultaten van een specifieke partij */
    public List<Result> findByPartyId(String partyId) {
        if (partyId == null) return List.of();
        return results.stream()
                .filter(r -> partyId.equals(r.getPartyId()))
                .collect(Collectors.toList());
    }

    /** 🔥 Bereken de top N partijen met de meeste stemmen */
    public List<Party> findTopParties(int limit) {
        if (results.isEmpty()) return List.of();

        // Groepeer alle stemmen per partij-ID
        Map<String, Long> votesByParty = results.stream()
                .filter(r -> r.getPartyId() != null)
                .collect(Collectors.groupingBy(
                        Result::getPartyId,
                        Collectors.summingLong(Result::getVotes)
                ));

        // Sorteer op aantal stemmen en pak de top N
        return votesByParty.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    String partyId = entry.getKey();
                    long votes = entry.getValue();

                    // Zoek de partij op (als bekend), anders maak een placeholder
                    Party baseParty = partiesById.getOrDefault(
                            partyId,
                            new Party(partyId, "Onbekende partij", "Onbekend", 0, "")
                    );

                    Party rankedParty = new Party(
                            baseParty.getId(),
                            baseParty.getName(),
                            baseParty.toString().contains("leider:") ? baseParty.toString() : baseParty.getName(),
                            (int) votes,
                            baseParty.toString().contains("site:") ? baseParty.toString() : ""
                    );
                    rankedParty.setVoteCount((int) votes);

                    return rankedParty;
                })
                .collect(Collectors.toList());
    }

    /** ✅ Vind de naam van een partij op basis van ID */
    public String getPartyName(String partyId) {
        if (partyId == null) return "Onbekende partij";
        Party party = partiesById.get(partyId);
        return (party != null) ? party.getName() : "Onbekende partij";
    }
}
