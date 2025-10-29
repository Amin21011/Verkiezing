package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    // Om data op te halen
    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    // Haalt alle kandidaten op via de repository
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public List<Candidate> getCandidatesByParty(String partyId) {
        return candidateRepository.findByPartyId(partyId);
    }

}
