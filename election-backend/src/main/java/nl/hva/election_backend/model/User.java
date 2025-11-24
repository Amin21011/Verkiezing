package nl.hva.election_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;
    private String password;
    private String role = "USER";

    @Column(nullable = true)
    private Boolean quizCompleted;
    private String quizBestMatch;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    public void setQuizCompleted(Boolean quizCompleted) {
        this.quizCompleted = quizCompleted;
    }

    public void setQuizBestMatch(String quizBestMatch) {
        this.quizBestMatch = quizBestMatch;
    }

    public String getQuizBestMatch() {
        return quizBestMatch;
    }

    public Boolean isQuizCompleted() {
        return quizCompleted;
    }
}
