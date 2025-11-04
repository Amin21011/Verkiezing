package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ForumRepository extends JpaRepository<ForumPost, Long> {
    List<ForumPost> findAllByOrderByPostedAtDesc();
}
