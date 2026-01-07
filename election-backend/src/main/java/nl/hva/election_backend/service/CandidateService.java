package nl.hva.election_backend.service;

import nl.hva.election_backend.dto.model.CandidateDTO;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    /**
     * Alle kandidaten (DTO)
     */
    public List<CandidateDTO> getAllCandidates() {
        return candidateRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Kandidaten per partij
     */
    public List<CandidateDTO> getCandidatesByParty(String partyId) {
        return candidateRepository.findByParty_Id(partyId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Vergelijk geselecteerde kandidaten
     */
    public List<CandidateDTO> compareCandidates(List<Map<String, String>> selections) {
        return candidateRepository.findAll()
                .stream()
                .filter(c -> selections.stream().anyMatch(s ->
                        c.getId().equals(s.get("candidateId")) &&
                                c.getParty() != null &&
                                c.getParty().getId().equals(s.get("partyId"))
                ))
                .map(this::toDto)
                .toList();
    }

    /**
     * Mapper: Entity → DTO
     */
    private CandidateDTO toDto(Candidate c) {
        return new CandidateDTO(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getParty() != null ? c.getParty().getId() : null,
                c.getParty() != null ? c.getParty().getName() : null,
                c.getGender(),
                c.getVotes()
        );
    }

    public List<CandidateDTO> getTopCandidatesByParty(String partyId) {
        List<CandidateDTO> candidates = candidateRepository.findByParty_IdOrderByVotesDesc(partyId)
                .stream()
                .sorted(Comparator.comparingInt(Candidate::getVotes).reversed())
                .limit(3)
                .map(this::toDto)
                .toList();

        // Fallback: als er GEEN kandidaten zijn
        if (candidates.isEmpty()) {
            return List.of(
                    new CandidateDTO(
                            "fallback-1",
                            "FALLBACK",        // shortCode
                            "Geen kandidaten",          // firstName
                            "beschikbaar",              // lastName
                            partyId,                    // partyId
                            partyId,                    // partyName
                            0                           // votes
                    ),
                    new CandidateDTO(
                            "fallback-2",
                            "FALLBACK",
                            "Geen kandidaten",
                            "beschikbaar",
                            partyId,
                            partyId,
                            0
                    ),
                    new CandidateDTO(
                            "fallback-3",
                            "FALLBACK",
                            "Geen kandidaten",
                            "beschikbaar",
                            partyId,
                            partyId,
                            0
                    )
            );
        }
        return candidates;
    }
}
