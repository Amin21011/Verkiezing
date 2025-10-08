package nl.hva.election_backend.model;
import java.util.ArrayList;
import java.util.List;

public class Election {
    private final String id;
    private final List<PartyDTO> parties = new ArrayList<>();
    private final List<CandidateDTO> candidates = new ArrayList<>();
    private final List<RegionDTO> regions = new ArrayList<>();
    private String name;
    private String date;

    public Election(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public List<PartyDTO> getParties() { return parties; }
    public List<CandidateDTO> getCandidates() { return candidates; }
    public List<RegionDTO> getRegions() { return regions; } // getter voor regio's

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public void addParty(PartyDTO party) {
        if (party != null) parties.add(party);
    }

    public void addCandidate(CandidateDTO candidate) {
        if (candidate != null) candidates.add(candidate);
    }

    public void addRegion(RegionDTO region) {
        if (region != null) regions.add(region);
    }

    public PartyDTO findPartyByName(String partyName) {
        return parties.stream()
                .filter(p -> p.getName().equalsIgnoreCase(partyName))
                .findFirst()
                .orElse(null);
    }

    public CandidateDTO findCandidateById(int id) {
        candidates.stream()
                .filter(c -> Boolean.parseBoolean(c.getId()))
                .findFirst();
        return null;
    }

    @Override
    public String toString() {
        return String.format(
                "Election %s (%s) with %d parties, %d candidates, %d regions",
                id, name != null ? name : "Unnamed", parties.size(), candidates.size(), regions.size()
        );
    }
}
