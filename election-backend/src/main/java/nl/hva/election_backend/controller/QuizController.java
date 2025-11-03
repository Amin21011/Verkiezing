package nl.hva.election_backend.controller;

import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.model.QuizResult;
import nl.hva.election_backend.service.QuizResultService;
import nl.hva.election_backend.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService = new QuizService();
    private final QuizResultService resultService = new QuizResultService();

    @GetMapping
    public Quiz getQuiz() {
        return quizService.getQuiz();
    }

    @PostMapping("/result")
    public QuizResult getResult(@RequestBody Map<String, String> userAnswers) {
        if (userAnswers == null || userAnswers.isEmpty()) {
            throw new IllegalArgumentException("Er zijn geen antwoorden ontvangen.");
        }
        return resultService.calculateResult(userAnswers);
    }
}
