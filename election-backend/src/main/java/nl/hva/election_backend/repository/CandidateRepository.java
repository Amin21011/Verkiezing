package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByPartyId(String partyId);
    boolean existsByCandidateId(String candidateId);
    List<Candidate> findByFirstNameContainingIgnoreCase(String name);
    List<Candidate> findByLastNameContainingIgnoreCase(String name);
}
