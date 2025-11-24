package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, String> {}
