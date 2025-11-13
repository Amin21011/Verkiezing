package nl.hva.election_backend.api;


import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.service.ForumPostService;
import nl.hva.election_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import nl.hva.election_backend.security.JwtUtil;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/forum")
public class ForumPostController {

    private final ForumPostService forumService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public ForumPostController(ForumPostService forumService, JwtUtil jwtUtil, UserService userService) {
        this.forumService = forumService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/posts")
    public List<ForumPost> getAllPosts() {
        return forumService.getAllPosts();
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

        return forumService.addPost(forumPost);
    }

}
