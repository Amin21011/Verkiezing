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

            // Voeg stemmen samen (optioneel)
            if (party.getVoteCount() > 0) {
                current.setVoteCount(current.getVoteCount() + party.getVoteCount());
            }
        } else {
            parties.add(party);
        }
    }

    public Optional<Party> findPartyById(String partyId) {
        return parties.stream()
                .filter(p -> p.getId().equals(partyId))
                .findFirst();
    }

    public void addCandidate(Candidate c) {
        if (c == null) return;

        if (!candidatesById.containsKey(c.getId())) {
            candidates.add(c);
            candidatesById.put(c.getId(), c);
            if (c.getShortCode() != null && !c.getShortCode().isBlank()) {
                candidatesByShortCode.put(c.getShortCode(), c);
            }
        }
    }

    public Optional<Candidate> getCandidateByShortCode(String shortCode) {
        if (shortCode == null || shortCode.isBlank()) return Optional.empty();
        Candidate c = candidatesByShortCode.get(shortCode);
        return c != null ? Optional.of(c) : Optional.empty();
    }

    public Optional<Candidate> getCandidateByCombinedId(String combinedId) {
        if (combinedId == null || combinedId.isBlank()) return Optional.empty();
        Candidate c = candidatesById.get(combinedId);
        return c != null ? Optional.of(c) : Optional.empty();
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
