package nl.hva.election_backend.model;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class ForumPost {
    private long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;
    private String title;
    private String content;
    private LocalDateTime postedAt = LocalDateTime.now();

    public ForumPost() {}

    public ForumPost(String username, String title, String content) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.postedAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
