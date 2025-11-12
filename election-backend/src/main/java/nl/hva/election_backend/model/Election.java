package nl.hva.election_backend.model;

import java.util.*;

/**
 * Houdt alle entiteiten van een verkiezing bij:
 * partijen, kandidaten en regio’s.
 */
public class Election {

    private final String id;
    private final List<Party> parties = new ArrayList<>();
    private final List<Candidate> candidates = new ArrayList<>();
    private final List<Region> regions = new ArrayList<>();

    private final Map<String, Candidate> candidatesById = new HashMap<>();
    private final Map<String, Candidate> candidatesByShortCode = new HashMap<>();

    public Election(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<Party> getParties() {
        return parties;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void addParty(Party party) {
        if (party == null || party.getId() == null) return;

        Optional<Party> existing = parties.stream()
                .filter(p -> p.getId().equals(party.getId()))
                .findFirst();

        if (existing.isPresent()) {
            Party current = existing.get();

            // Voeg stemmen samen als er al data bestaat
            if (party.getVoteCount() > 0) {
                current.setVoteCount(current.getVoteCount() + party.getVoteCount());
            }
        } else {
            parties.add(party);
        }
    }

    public Optional<Party> findPartyById(String partyId) {
        if (partyId == null) return Optional.empty();
        return parties.stream()
                .filter(p -> p.getId().equals(partyId))
                .findFirst();
    }

    public void addCandidate(Candidate c) {
        if (c == null || c.getId() == null || c.getId().isBlank()) return;

        if (!candidatesById.containsKey(c.getId())) {
            candidates.add(c);
            candidatesById.put(c.getId(), c);

            if (c.getShortCode() != null && !c.getShortCode().isBlank()) {
                candidatesByShortCode.putIfAbsent(c.getShortCode(), c);
            }
        } else {
             System.out.printf("Dubbele kandidaat overgeslagen (Id=%s)%n", c.getId());
        }
    }

    public Optional<Candidate> getCandidateById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return Optional.ofNullable(candidatesById.get(id));
    }

    public void addRegion(Region region) {
        if (region == null) return;

        boolean exists = regions.stream()
                .anyMatch(r -> Objects.equals(r.getNumber(), region.getNumber())
                        || Objects.equals(r.getName(), region.getName()));

        if (!exists) {
            regions.add(region);
        }
    }
}
