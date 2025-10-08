package nl.hva.election_backend.repository;
import nl.hva.election_backend.model.PartyDTO;

import java.util.ArrayList;
import java.util.List;

public class PartyRepository {
    private final List<PartyDTO> parties = new ArrayList<>();

    public void saveAll(List<PartyDTO> partyList) {
        parties.clear();
        parties.addAll(partyList);
    }

    public List<PartyDTO> getAll() {
        return new ArrayList<>(parties);
    }
}
