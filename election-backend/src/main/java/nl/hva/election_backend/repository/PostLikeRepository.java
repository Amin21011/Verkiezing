package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.model.PostLike;
import nl.hva.election_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, ForumPost post);
}
