package nl.hva.election_backend.repository;
import nl.hva.election_backend.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, String> {
    List<Party> findByNameContainingIgnoreCase(String name);
}