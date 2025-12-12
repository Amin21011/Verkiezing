package nl.hva.election_backend.repository;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Party1;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Party> findById(String id) {
        for (Party p : parties) {
            if (p.getId().equals(id)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }


    public List<Party> findByNameContainingIgnoreCase(String name) {
        List<Party> result = new ArrayList<>();
        for (Party p : parties) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }
}
