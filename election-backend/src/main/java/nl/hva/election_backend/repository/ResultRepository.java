package nl.hva.election_backend.repository;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Result;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ResultRepository {
    private final List<Result> results = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Party> partiesById = new HashMap<>();

    public synchronized void addResult(Result result) {
        if (result == null) return;

        Optional<Result> existing = results.stream()
                .filter(r ->
                        Objects.equals(r.getPartyId(), result.getPartyId()) &&
                                Objects.equals(r.getCandidateId(), result.getCandidateId()) &&
                                Objects.equals(r.getRegionId(), result.getRegionId()) &&
                                Objects.equals(r.getRegionType(), result.getRegionType()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setVotes(existing.get().getVotes() + result.getVotes());
        } else {
            results.add(result);
        }
    }

    public List<Result> getAll() {
        synchronized (results) {
            return new ArrayList<>(results);
        }
    }

    public void registerParties(List<Party> parties) {
        partiesById.clear();
        if (parties != null) {
            for (Party p : parties) {
                partiesById.put(p.getId(), p);
            }
        }
    }

    public List<Party> getTopParties(int limit) {
        if (results.isEmpty()) return List.of();

        Map<String, Integer> votesByParty = new HashMap<>();
        for (Result r : results) {
            if (r.getPartyId() != null) {
                votesByParty.merge(r.getPartyId(), r.getVotes(), Integer::sum);
            }
        }

        return votesByParty.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    String id = entry.getKey();
                    int votes = entry.getValue();
                    Party base = partiesById.get(id);
                    if (base == null)
                        return new Party(id, "Onbekende partij", "", votes, "");
                    Party p = new Party(id, base.getName(), base.getLeaderName(), votes, base.getWebsite());
                    p.setVoteCount(votes);
                    return p;
                })
                .collect(Collectors.toList());
    }

    public String getPartyName(String partyId) {
        if (partyId == null) return "Onbekende partij";
        Party p = partiesById.get(partyId);
        return (p != null) ? p.getName() : "Onbekende partij";
    }

    public void setPartyNames(List<Party> parties) {
        registerParties(parties);
    }

    public synchronized void clear() {
        results.clear();
        partiesById.clear();
    }
}
