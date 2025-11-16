package nl.hva.election_backend.service;

import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.repository.ForumPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumPostService {

    private final ForumPostRepository forumRepository;

    public ForumPostService(ForumPostRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    public List<ForumPost> getAllPosts() {
        return forumRepository.findAllByOrderByPostedAtDesc();
    }

    public ForumPost addPost(ForumPost forumPost) {
        return forumRepository.save(forumPost);
    }

    public Optional<ForumPost> getPostById(long id) {
        return forumRepository.findById(id);
    }
}
