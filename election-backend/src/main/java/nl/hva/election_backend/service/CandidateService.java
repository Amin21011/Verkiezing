package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public List<Candidate> getCandidatesByParty(String partyId) {
        return candidateRepository.findByPartyId(partyId);
    }

    public List<Candidate> compareCandidates(
            List<Map<String, String>> selections
    ) {
        return candidateRepository.findByPartyAndCandidateIds(selections);
    }

}
