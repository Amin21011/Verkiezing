package nl.hva.election_backend.api;

import nl.hva.election_backend.dto.model.AnswerDTO;
import nl.hva.election_backend.model.Question;
import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.model.QuizResult;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.QuestionService;
import nl.hva.election_backend.service.QuizResultService;
import nl.hva.election_backend.service.QuizService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final QuizResultService quizResultService;
    private final QuestionService questionService;
    private final JwtUtil jwtUtil;

    public QuizController(
            QuizService quizService,
            QuizResultService quizResultService,
            JwtUtil jwtUtil,
            QuestionService questionService
    ) {
        this.quizService = quizService;
        this.quizResultService = quizResultService;
        this.jwtUtil = jwtUtil;
        this.questionService = questionService;
    }

    @GetMapping
    public Quiz getQuiz() {
        return quizService.getQuiz();
    }

    @PostMapping("/result")
    public QuizResult submitQuiz(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> answers
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Geen geldige Authorization header ontvangen.");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);
        return quizResultService.processQuiz(answers, email);
    }

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/questions/{id}")
    public Question getQuestionById(@PathVariable String id) {
        return questionService.getQuestionById(id);
    }

    @RestController
    @RequestMapping("/candidates")

    public class CandidateController {
        @GetMapping("/{id}/bio")
        public String getCandidateBio(@PathVariable String id) {
            return "Biography unavailable";
        }
    }
}