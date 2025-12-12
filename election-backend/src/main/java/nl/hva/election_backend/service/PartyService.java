package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PartyService {

    private final PartyRepository partyRepository;

    public PartyService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public List<Party> getTopPartiesByYear(Integer year, int limit) {
        List<Party> all = partyRepository.findAll();

        return all.stream()
                .sorted(Comparator.comparingInt(Party::getVoteCount).reversed())
                .limit(limit)
                .toList();
    }

    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }
}
