package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.CandidateRepository;
import nl.hva.election_backend.repository.PartyRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    private final CandidateRepository candidateRepository;
    private final PartyRepository partyRepository;

    public SearchService(CandidateRepository candidateRepository, PartyRepository partyRepository) {
        this.candidateRepository = candidateRepository;
        this.partyRepository = partyRepository;
    }

    public List<Candidate> searchCandidates(String name) {
        List<Candidate> first = candidateRepository.findByFirstNameContainingIgnoreCase(name);
        List<Candidate> last = candidateRepository.findByLastNameContainingIgnoreCase(name);

        Set<Candidate> combined = new LinkedHashSet<>();
        combined.addAll(first);
        combined.addAll(last);

        for (Candidate c : combined) {
            partyRepository.findById(c.getPartyId())
                    .ifPresent(p -> c.setPartyName(p.getName()));
        }

        return new ArrayList<>(combined);
    }

    public List<Party> searchParties(String name) {
        return partyRepository.findByNameContainingIgnoreCase(name);}
}
