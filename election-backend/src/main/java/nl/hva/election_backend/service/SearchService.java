package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    private final CandidateRepository candidateRepository;

    public SearchService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> search(String name) {

        List<Candidate> first = candidateRepository.findByFirstNameContainingIgnoreCase(name);
        List<Candidate> last = candidateRepository.findByLastNameContainingIgnoreCase(name);

        Set<Candidate> combined = new LinkedHashSet<>();
        combined.addAll(first);
        combined.addAll(last);

        return new ArrayList<>(combined);
    }
}
