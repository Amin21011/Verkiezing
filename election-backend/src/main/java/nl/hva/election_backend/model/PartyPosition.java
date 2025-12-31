package nl.hva.election_backend.model;

import jakarta.persistence.*;

@Entity
public class PartyPosition {

    @Id
    @GeneratedValue
    private Long id;

    private String party;

    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne
    private Question question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
