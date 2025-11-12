package nl.hva.election_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class ForumPost {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;
    private LocalDateTime postedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public ForumPost() {}

    public ForumPost(String title, String content) {
        this.title = title;
        this.content = content;
        this.postedAt = LocalDateTime.now();

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
}
