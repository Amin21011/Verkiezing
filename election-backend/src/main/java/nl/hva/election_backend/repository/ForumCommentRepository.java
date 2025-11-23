package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {
    List<ForumComment> findAllByOrderByCreatedAtDesc();

}
