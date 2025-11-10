package nl.hva.election_backend.model;

import java.util.List;

public class Quiz {
    private final List<Question> questions;

    // Constructor met List<Question>
    public Quiz(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}