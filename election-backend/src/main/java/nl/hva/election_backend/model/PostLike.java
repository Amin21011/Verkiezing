package nl.hva.election_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private ForumPost post;

    private int value;

    public PostLike() {}

    public PostLike(User user, ForumPost post, int value) {
        this.user = user;
        this.post = post;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ForumPost getPost() {
        return post;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
