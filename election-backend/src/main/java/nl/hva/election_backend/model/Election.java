package nl.hva.election_backend.model;

import java.util.ArrayList;
import java.util.List;

public class Election {
    private final String id;
    private final List<PartyDTO> parties = new ArrayList<>();

    public Election(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public List<PartyDTO> getParties() { return parties; }

    public void addParty(PartyDTO party) {
        this.parties.add(party);
    }

    @Override
    public String toString() {
        return "Election " + id + " with " + parties.size() + " parties";
    }
}
