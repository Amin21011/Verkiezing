package nl.hva.election_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class ForumPost {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;
    private LocalDateTime postedAt = LocalDateTime.now();
    private int likeCount = 0;
    private int dislikeCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ForumComment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "topic_id")
    @JsonIgnoreProperties("posts")
    private Topic topic;

    public ForumPost() {}

    public ForumPost(String title, String content, int likeCount, int dislikeCount) {
        this.title = title;
        this.content = content;
        this.postedAt = LocalDateTime.now();
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;

    }

    public long getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }
    public User getUser() { return user; }

    public Topic getTopic() { return topic; }

    public List<ForumComment> getComments() {
        return comments;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setComments(List<ForumComment> comments) {
        this.comments = comments;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
