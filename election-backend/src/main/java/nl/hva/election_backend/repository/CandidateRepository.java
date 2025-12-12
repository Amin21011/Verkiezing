package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
    List<Candidate> findByParty_Id(String partyId);
    List<Candidate> findByFirstNameContainingIgnoreCase(String name);
    List<Candidate> findByLastNameContainingIgnoreCase(String name);
}
