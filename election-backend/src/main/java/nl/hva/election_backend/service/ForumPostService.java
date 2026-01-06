package nl.hva.election_backend.service;

import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.model.PostLike;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.ForumPostRepository;
import nl.hva.election_backend.repository.PostLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumPostService {

    private final ForumPostRepository forumRepository;
    private final UserService userService;
    private final PostLikeRepository postLikeRepository;

    public ForumPostService(ForumPostRepository forumRepository, UserService userService, PostLikeRepository postLikeRepository) {
        this.forumRepository = forumRepository;
        this.userService = userService;
        this.postLikeRepository = postLikeRepository;
    }

    public List<ForumPost> getAllPosts() {
        return forumRepository.findAllByOrderByPostedAtDesc();
    }

    public ForumPost addPost(ForumPost forumPost) {
        return forumRepository.save(forumPost);
    }

    public void deletePost(long postId) {
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post niet gevonden"));
        forumRepository.delete(post);
    }

    public ForumPost likePost(long postId, String email) {
        User user = userService.findByEmail(email);
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<PostLike> existing = postLikeRepository.findByUserAndPost(user, post);

        if (existing.isPresent()) {
            PostLike like = existing.get();

            if (like.getValue() == 1) {
                return post;
            }

            post.setDislikeCount(post.getDislikeCount() - 1);
            like.setValue(1);
        } else {
            PostLike newLike = new PostLike(user, post, 1);
            postLikeRepository.save(newLike);
        }

        post.setLikeCount(post.getLikeCount() + 1);

        return forumRepository.save(post);
    }

    public ForumPost dislikePost(long postId, String email) {
        User user = userService.findByEmail(email);
        ForumPost post = forumRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<PostLike> existing = postLikeRepository.findByUserAndPost(user, post);

        if (existing.isPresent()) {
            PostLike like = existing.get();

            if (like.getValue() == -1) {
                return post;
            }


            post.setLikeCount(post.getLikeCount() - 1);
            like.setValue(-1);
        } else {
            PostLike newLike = new PostLike(user, post, -1);
            postLikeRepository.save(newLike);
        }

        post.setDislikeCount(post.getDislikeCount() + 1);

        return forumRepository.save(post);
    }


    public Optional<ForumPost> getPostById(long id) {
        return forumRepository.findById(id);
    }
}
