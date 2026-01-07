package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.ConstituencyVotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConstituencyVotesRepository extends JpaRepository<ConstituencyVotes, Long> {


    List<ConstituencyVotes> findByYear(int year);
}
