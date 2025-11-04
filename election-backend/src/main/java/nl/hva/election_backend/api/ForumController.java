package nl.hva.election_backend.api;

import nl.hva.election_backend.model.ForumPost;
import nl.hva.election_backend.service.ForumService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/forum")
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("/posts")
    public List<ForumPost> getAllPosts() {
        return forumService.getAllPosts();
    }

    @PostMapping("/posts")
    public ForumPost createdPost(@RequestBody ForumPost forumPost) {
        return forumService.addPost(forumPost);
    }
}
