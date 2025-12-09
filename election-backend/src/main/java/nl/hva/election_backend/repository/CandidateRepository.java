package nl.hva.election_backend.repository;

import jakarta.annotation.PostConstruct;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.utils.xml.transformers.ResultLoader;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CandidateRepository {

    private final List<Candidate> candidates = new ArrayList<>();
    private final List<Party> parties = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            Election election = new Election("Tweede Kamerverkiezingen 2023");
            ResultRepository repository = new ResultRepository();
            ResultLoader.loadResults(election, repository);

            // Sla lokaal op
            candidates.addAll(election.getCandidates());
            parties.addAll(election.getParties());

            // Voeg partijnaam toe aan elke kandidaat
            for (Candidate c : candidates) {
                Party p = parties.stream()
                        .filter(party -> party.getId().equals(c.getPartyId()))
                        .findFirst()
                        .orElse(null);
                if (p != null) {
                    c.setPartyName(p.getName());
                }
            }

            System.out.println("Parsed " + candidates.size() + " kandidaten en " + parties.size() + " partijen");
        } catch (Exception e) {
            System.err.println("Fout bij inladen kandidaten: " + e.getMessage());
        }
    }

    public List<Candidate> findAll() {
        return candidates;
    }

    public List<Candidate> findByPartyId(String partyId) {
        return candidates.stream()
                .filter(c -> c.getPartyId().equals(partyId))
                .toList();
    }

    public List<Party> findAllParties() {
        return parties;
    }

    public List<Candidate> findByFirstNameContainingIgnoreCase(String name) {
        return candidates.stream()
                .filter(c -> c.getFirstName() != null &&
                        c.getFirstName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public List<Candidate> findByLastNameContainingIgnoreCase(String name) {
        return candidates.stream()
                .filter(c -> c.getLastName() != null &&
                        c.getLastName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
    public List<Candidate> findByPartyAndCandidateIds(
            List<Map<String, String>> selections
    ) {
        return candidates.stream()
                .filter(c ->
                        selections.stream().anyMatch(s ->
                                c.getId().equals(s.get("candidateId")) &&
                                        c.getPartyId().equals(s.get("partyId"))
                        )
                )
                .toList();
    }

}
