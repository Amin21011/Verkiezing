package nl.hva.election_backend.service;

import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.repository.ForumRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumService {

    private final ForumRepository forumRepository;

    public ForumService(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    public List<ForumPost> getAllPosts() {
        return forumRepository.findAllByOrderByPostedAtDesc();
    }

    public ForumPost addPost(ForumPost forumPost) {
        return forumRepository.save(forumPost);
    }
}
