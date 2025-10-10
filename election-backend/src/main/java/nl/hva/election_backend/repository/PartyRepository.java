package nl.hva.election_backend.repository;
import nl.hva.election_backend.model.Party;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class PartyRepository {
    private final List<Party> parties = new ArrayList<>();

    public void saveAll(List<Party> partyList) {
        parties.clear();
        parties.addAll(partyList);
    }

    public List<Party> getAll() {
        return new ArrayList<>(parties);
    }
}
