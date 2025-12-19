package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.Constituencies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConstituenciesRepository extends JpaRepository<Constituencies,Long> {

    Optional<Constituencies> findByName(String name);
}
