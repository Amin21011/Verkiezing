package nl.hva.election_backend.api;


import nl.hva.election_backend.model.ForumComment;
import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.service.ForumCommentService;
import nl.hva.election_backend.service.ForumPostService;
import nl.hva.election_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import nl.hva.election_backend.security.JwtUtil;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    private final ForumPostService forumPostService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ForumCommentService forumCommentService;

    public ForumPostController(ForumPostService forumPostService, JwtUtil jwtUtil, UserService userService, ForumCommentService forumCommentService) {
        this.forumPostService = forumPostService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.forumCommentService = forumCommentService;
    }

    @GetMapping("/posts")
    public List<ForumPost> getAllPosts() {
        return forumPostService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<ForumPost> getPostById(@PathVariable long id) {
        return forumPostService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ForumPost addPost(
            @RequestBody ForumPost forumPost,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Geen JWT token meegegeven");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        User user = userService.findByEmail(email);

        forumPost.setUser(user);
        forumPost.setPostedAt(LocalDateTime.now());

        return forumPostService.addPost(forumPost);
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<ForumComment> addComment(
            @PathVariable long id,
            @RequestBody ForumComment forumComment,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        ForumComment saved = forumCommentService.addComment(id, forumComment, email);
        return  ResponseEntity.ok(saved);
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<ForumPost> likePost(
            @PathVariable long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        ForumPost updated = forumPostService.likePost(id, email);

        return ResponseEntity.ok(updated);
    }


    @PostMapping("/posts/{id}/dislike")
    public ResponseEntity<ForumPost> dislikePost(
            @PathVariable long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        ForumPost updated = forumPostService.dislikePost(id, email);

        return ResponseEntity.ok(updated);
    }


}
