package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
    List<Candidate> findByParty_Id(String partyId);
    List<Candidate> findByFirstNameContainingIgnoreCase(String name);
    List<Candidate> findByLastNameContainingIgnoreCase(String name);
    List<Candidate> findByParty_IdOrderByVotesDesc(String partyId);
    @Query("""
    SELECT c
    FROM Candidate c
    ORDER BY c.votes DESC
""")
    List<Candidate> findTopCandidates();
    }
