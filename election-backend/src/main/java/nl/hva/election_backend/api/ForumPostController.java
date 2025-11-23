package nl.hva.election_backend.api;

import nl.hva.election_backend.model.ForumComment;
import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.service.ForumCommentService;
import nl.hva.election_backend.service.ForumPostService;
import nl.hva.election_backend.service.UserService;
import nl.hva.election_backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    private final ForumPostService forumPostService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ForumCommentService forumCommentService;

    public ForumPostController(
            ForumPostService forumPostService,
            JwtUtil jwtUtil,
            UserService userService,
            ForumCommentService forumCommentService
    ) {
        this.forumPostService = forumPostService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.forumCommentService = forumCommentService;
    }

    private String extractEmail(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("401");
        }
        String token = authHeader.substring(7);
        return jwtUtil.validateTokenAndGetEmail(token);
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
    public ResponseEntity<?> addPost(
            @RequestBody ForumPost forumPost,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            String email = extractEmail(authHeader);
            User user = userService.findByEmail(email);
            forumPost.setUser(user);
            forumPost.setPostedAt(LocalDateTime.now());
            ForumPost saved = forumPostService.addPost(forumPost);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Je moet ingelogd zijn");
        }
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> addComment(
            @PathVariable long id,
            @RequestBody ForumComment forumComment,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            String email = extractEmail(authHeader);
            ForumComment saved = forumCommentService.addComment(id, forumComment, email);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Je moet ingelogd zijn");
        }
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<?> likePost(
            @PathVariable long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            String email = extractEmail(authHeader);
            ForumPost updated = forumPostService.likePost(id, email);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Je moet ingelogd zijn");
        }
    }

    @PostMapping("/posts/{id}/dislike")
    public ResponseEntity<?> dislikePost(
            @PathVariable long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        try {
            String email = extractEmail(authHeader);
            ForumPost updated = forumPostService.dislikePost(id, email);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Je moet ingelogd zijn");
        }
    }
}
