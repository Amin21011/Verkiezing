package nl.hva.election_backend.service;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Party1;
import nl.hva.election_backend.repository.PartyRepository;
import nl.hva.election_backend.repository.ResultRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PartyService {
    private final PartyRepository repository;
    private final ResultRepository resultRepository;

    public PartyService(PartyRepository repository, ResultRepository resultRepository) {
        this.repository = repository;
        this.resultRepository = resultRepository;
    }

    public List<Party1> getAllPartiesRandomized() {
        List<Party1> shuffled = new ArrayList<>(repository.getAllParties());
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public List<Party> getTopPartiesByYear(Integer year, int limit) {
        System.out.println("Top partijen opgevraagd (jaar = " + year + ", limit = " + limit + ")");
        List<Party> topParties = resultRepository.findTopParties(limit);

        if (topParties == null || topParties.isEmpty()) {
            System.out.println("Geen resultaten gevonden");
            return new ArrayList<>();
        }
        return topParties;
    }
}
