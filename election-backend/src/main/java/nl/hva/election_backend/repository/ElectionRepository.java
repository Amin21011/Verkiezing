package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepository extends JpaRepository<Election, String> {
}
