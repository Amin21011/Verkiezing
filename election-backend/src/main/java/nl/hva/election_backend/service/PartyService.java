package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.PartyRepository;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.DutchPartyParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyService {
    private final PartyRepository repository;
    private final ResultRepository resultRepository; // injecteren
    private final DutchPartyParser transformer = new DutchPartyParser();

    public PartyService(PartyRepository repository, ResultRepository resultRepository) {
        this.repository = repository;
        this.resultRepository = resultRepository;
    }

    public void loadParties(String xmlFileName) {
        List<Party> parsedParties = transformer.parseParties(xmlFileName);
        repository.saveAll(parsedParties);
        // partiesById zodat findTopParties de namen kan vinden
        resultRepository.registerParties(parsedParties);
    }

    public List<Party> getAllParties() {
        return repository.getAll();
    }
}
