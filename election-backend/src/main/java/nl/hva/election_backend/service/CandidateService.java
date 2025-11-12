package nl.hva.election_backend.service;

import jakarta.annotation.PostConstruct;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.repository.CandidateRepository;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.transformers.ResultLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

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


    @PostConstruct
    public void init() {
        try {
            Election election = new Election("Tweede Kamerverkiezingen 2023");
            ResultLoader.loadResults(election, new ResultRepository());

            List<Candidate> candidates = election.getCandidates();

            for (Candidate c : candidates) {
                if (!candidateRepository.existsByCandidateId(c.getId())) {
                    candidateRepository.save(c);
                }
            }

            System.out.println("✅ " + candidates.size() + " kandidaten opgeslagen in database");
        } catch (Exception e) {
            System.err.println("❌ Fout bij inladen kandidaten: " + e.getMessage());
        }
    }
}
