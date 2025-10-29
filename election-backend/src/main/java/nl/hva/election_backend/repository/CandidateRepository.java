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

    // Het uitlezen van de XML met kandidaten
    private final DutchCandidateParser candidateParser;

    // Het opslaan van kandidaten op in een lijst
    private List<Candidate> candidates = new ArrayList<>();

    // De parser wordt via dependency injection binnengehaald door Spring
    public CandidateRepository(DutchCandidateParser candidateParser) {
        this.candidateParser = candidateParser;
    }

    // Hier worden de kandidaten uit het XML-bestand ingeladen
    @PostConstruct
    public void init() {
        List<Party> parties = new ArrayList<>();
        candidates = candidateParser.parseCandidates(
                "TK2023_HvA_UvA/Kandidatenlijsten_TK2023_Amsterdam.eml.xml",
                parties
        );
        System.out.println("✅ Loaded " + candidates.size() + " candidates from XML file");
    }

    // Geeft alle kandidaten terug
    public List<Candidate> findAll() {
        return candidates;
    }

    // Zoeken van kandidaten via partij-ID
    public List<Candidate> findByPartyId(String partyId) {
        return candidates.stream()
                .filter(c -> c.getPartyId() != null && c.getPartyId().equalsIgnoreCase(partyId))
                .collect(Collectors.toList());
    }
}
