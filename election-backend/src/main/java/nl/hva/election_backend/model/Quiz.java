package nl.hva.election_backend.model;

import nl.hva.election_backend.service.QuestionService;

import java.util.List;

public class Quiz {
    private final List<QuestionService> questions;

    // Constructor met List<Question>
    public Quiz(List<QuestionService> questions) {
        this.questions = questions;
    }

    public List<QuestionService> getQuestions() {
        return questions;
    }
}
