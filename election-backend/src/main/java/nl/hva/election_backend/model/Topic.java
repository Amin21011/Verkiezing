package nl.hva.election_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ForumPost> posts = new ArrayList<>();

    public Topic() {}

    public Topic(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<ForumPost> getPosts() { return posts; }

    public void setName(String name) { this.name = name; }
    public void setPosts(List<ForumPost> posts) { this.posts = posts; }
}
