package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Topic;
import nl.hva.election_backend.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic addTopic(Topic topic) {
        // Duplicate check
        if (topicRepository.findByNameIgnoreCase(topic.getName()).isPresent()) {
            throw new IllegalArgumentException("Topic naam bestaat al");
        }
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }
}
