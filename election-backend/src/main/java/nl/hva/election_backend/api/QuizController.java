package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Question;
import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.model.QuizResult;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.QuestionService;
import nl.hva.election_backend.service.QuizResultService;
import nl.hva.election_backend.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;
    private final QuizResultService resultService;
    private final QuestionService questionService;
    private final JwtUtil jwtUtil;

    public QuizController(QuizResultService resultService, JwtUtil jwtUtil, QuizService quizService, QuestionService questionService) {
        this.resultService = resultService;
        this.jwtUtil = jwtUtil;
        this.quizService = quizService;
        this.questionService = questionService;
    }

    @GetMapping
    public Quiz getQuiz() {
        return quizService.getQuiz();
    }

    @PostMapping("/api/result")
    public QuizResult getResult(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> userAnswers
    ) {
        if (userAnswers == null || userAnswers.isEmpty()) {
            throw new IllegalArgumentException("Er zijn geen antwoorden ontvangen.");
        }

        int totalQuestions = quizService.getQuiz().getQuestions().size();
        if (userAnswers.size() != totalQuestions) {
            throw new IllegalArgumentException("Niet alle vragen zijn beantwoord (" +
                    userAnswers.size() + " van " + totalQuestions + ").");
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Geen geldige token ontvangen.");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        QuizResult result = resultService.calculateResult(userAnswers);
        resultService.saveQuizResult(email, result.getBestMatch());

        return result;
    }

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/questions/{id}")
    public Question getQuestionById(@PathVariable String id) {
        return questionService.getQuestionById(id);
    }
}
