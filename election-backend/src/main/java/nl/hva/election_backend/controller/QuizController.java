package nl.hva.election_backend.controller;

import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.service.QuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService = new QuizService();

    @GetMapping
    public Quiz getQuiz() {
        return quizService.getQuiz();
    }
}
