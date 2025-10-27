package nl.hva.election_backend.repository;

import jakarta.annotation.PostConstruct;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.utils.xml.DutchCandidateParser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CandidateRepository {

    private final DutchCandidateParser candidateParser;
    private List<Candidate> candidates = new ArrayList<>();

    public CandidateRepository(DutchCandidateParser candidateParser) {
        this.candidateParser = candidateParser;
    }

    @PostConstruct
    public void init() {
        List<Party> parties = new ArrayList<>();
        candidates = candidateParser.parseCandidates("TK2023_HvA_UvA/Kandidatenlijsten_TK2023_Amsterdam.eml.xml", parties);
        System.out.println("✅ Loaded " + candidates.size() + " candidates from XML file");
    }

    public List<Candidate> findAll() {
        return candidates;
    }

    public List<Candidate> findByPartyId(String partyId) {
        return candidates.stream()
                .filter(c -> c.getPartyId() != null && c.getPartyId().equalsIgnoreCase(partyId))
                .collect(Collectors.toList());
    }
}
