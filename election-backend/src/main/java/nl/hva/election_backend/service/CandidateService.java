package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return candidateRepository.findAll().stream()
                .filter(c -> c.getParty() != null && c.getParty().getId().equals(partyId))
                .collect(Collectors.toList());
    }

    /**
     * Vergelijk kandidaten op basis van geselecteerde candidateId en partyId
     */
    public List<Candidate> compareCandidates(List<Map<String, String>> selections) {
        return candidateRepository.findAll().stream()
                .filter(c -> selections.stream()
                        .anyMatch(s ->
                                c.getId().equals(s.get("candidateId")) &&
                                        c.getParty() != null &&
                                        c.getParty().getId().equals(s.get("partyId"))
                        )
                )
                .collect(Collectors.toList());
    }
}
