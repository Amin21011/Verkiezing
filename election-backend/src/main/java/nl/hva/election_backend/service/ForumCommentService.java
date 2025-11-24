package nl.hva.election_backend.service;

import nl.hva.election_backend.model.ForumComment;
import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.ForumCommentRepository;
import nl.hva.election_backend.repository.ForumPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumCommentService {

    private final ForumCommentRepository forumCommentRepository;
    private final ForumPostRepository forumPostRepository;
    private final UserService userService;

    public ForumCommentService(
            ForumCommentRepository forumCommentRepository,
            ForumPostRepository forumPostRepository,
            UserService userService
    ) {
        this.forumCommentRepository = forumCommentRepository;
        this.forumPostRepository = forumPostRepository;
        this.userService = userService;
    }

    public ForumComment addComment(long postId, ForumComment forumComment, String email) {

        User user = userService.findByEmail(email);

        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("post not found"));

        forumComment.setUser(user);
        forumComment.setForumPost(post);

        return forumCommentRepository.save(forumComment);
    }
}
