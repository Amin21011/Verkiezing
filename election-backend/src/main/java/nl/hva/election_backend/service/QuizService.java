package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.repository.QuizRepository;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz getQuiz() {
        return quizRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Quiz niet gevonden"));
    }
}
