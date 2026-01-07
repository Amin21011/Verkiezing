package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.PartyPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyPositionRepository extends JpaRepository<PartyPosition, Long> {}

