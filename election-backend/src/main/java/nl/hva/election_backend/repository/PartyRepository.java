package nl.hva.election_backend.repository;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Party1;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PartyRepository {
    private final List<Party> parties = new ArrayList<>();
    private final List<Party1> partiesLong = new ArrayList<>();

    public List<Party> getAll() {
        return new ArrayList<>(parties);
    }

    public List<Party1> getAllParties() {
        return new ArrayList<>(partiesLong);
    }
}
