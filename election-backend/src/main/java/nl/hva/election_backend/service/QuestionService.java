package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Question;
import nl.hva.election_backend.model.Quiz;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuizService quizService = new QuizService();

    public List<Question> getAllQuestions() {
        Quiz quiz = quizService.getQuiz();
        return quiz.getQuestions();
    }

    public Question getQuestionById(String id) {
        return getAllQuestions().stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vraag met ID " + id + " niet gevonden"));
    }
}
