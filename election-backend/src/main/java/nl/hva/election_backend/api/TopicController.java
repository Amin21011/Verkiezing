package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Topic;
import nl.hva.election_backend.service.TopicService;
import nl.hva.election_backend.dto.model.TopicDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        return topicService.getTopicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Topic> addTopic(@RequestBody TopicDTO dto) {
        Topic topic = new Topic(dto.getName());
        Topic saved = topicService.addTopic(topic);
        return ResponseEntity.ok(saved);
    }
}
