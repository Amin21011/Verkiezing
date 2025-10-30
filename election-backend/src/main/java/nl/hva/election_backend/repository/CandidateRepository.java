package nl.hva.election_backend.repository;

import jakarta.annotation.PostConstruct;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.utils.xml.transformers.ResultLoader;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateRepository {

    // Lijsten om alle kandidaten en partijen opslaan
    private final List<Candidate> candidates = new ArrayList<>();
    private final List<Party> parties = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            Election election = new Election("Tweede Kamerverkiezingen 2023");

            ResultRepository repository = new ResultRepository();

            // Laad verkiezingsresultaten van XML
            ResultLoader.loadResults(election, repository);

            // Toevoegen geladen kandidaten/partijen toe aan lijsten
            candidates.addAll(election.getCandidates());
            parties.addAll(election.getParties());

            System.out.println("✅ Parsed " + candidates.size() + " kandidaten en " + parties.size() + " partijen");
        } catch (Exception e) {
            System.err.println("❌ Fout bij inladen kandidaten: " + e.getMessage());
        }
    }

    // Geeft alle kandidaten terug
    public List<Candidate> findAll() {
        return candidates;
    }

    // Geeft alle kandidaten van een specifieke partij terug
    public List<Candidate> findByPartyId(String partyId) {
        return candidates.stream()
                .filter(c -> c.getPartyId().equals(partyId))
                .toList();
    }

    // Geeft alle partijen terug
    public List<Party> findAllParties() {
        return parties;
    }
}
