package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.PartyRepository;
import nl.hva.election_backend.utils.xml.DutchPartyParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyService {
    private final PartyRepository repository;
    private final DutchPartyParser transformer = new DutchPartyParser();

    public PartyService(PartyRepository repository) {
        this.repository = repository;
    }

    public void loadParties(String xmlFileName) {
        // laad de partijen vanuit resources
        List<Party> parsedParties = transformer.parseParties(xmlFileName);
        repository.saveAll(parsedParties);
    }

    public List<Party> getAllParties() {
        return repository.getAll();
    }
}
