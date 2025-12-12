package nl.hva.election_backend.service;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.repository.PartyRepository;
import nl.hva.election_backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {
    private final ResultRepository resultRepository;
    private final PartyRepository partyRepository;
    private final CandidateRepository candidateRepository;

    public ResultService(
            ResultRepository resultRepository,
            PartyRepository partyRepository,
            CandidateRepository candidateRepository
    ) {
        this.resultRepository = resultRepository;
        this.partyRepository = partyRepository;
        this.candidateRepository = candidateRepository;
    }

    public List<Party> getTopParties(int limit) {
        return partyRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Party::getVoteCount).reversed())
                .limit(limit)
                .toList();
    }

    public List<Candidate> getTopCandidatesByParty(String partyId, int limit) {
        return candidateRepository.findAll().stream()
                .filter(c -> c.getParty() != null && c.getParty().getId().equals(partyId))
                .sorted(Comparator.comparingInt(Candidate::getVotes).reversed())
                .limit(limit)
                .toList();
    }

    public Party getPartyById(String id) {
        return partyRepository.findById(id).orElse(null);
    }

    public Map<String, Integer> getVotesByParty() {
        return partyRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Party::getId,
                        Party::getVoteCount
                ));
    }
}
