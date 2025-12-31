package nl.hva.election_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne
    @JsonIgnore
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<PartyPosition> partyPositions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<PartyPosition> getPartyPositions() {
        return partyPositions;
    }

    public void setPartyPositions(List<PartyPosition> partyPositions) {
        this.partyPositions = partyPositions;
    }
}
