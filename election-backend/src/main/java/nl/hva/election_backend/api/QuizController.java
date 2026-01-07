package nl.hva.election_backend.api;

import nl.hva.election_backend.model.Question;
import nl.hva.election_backend.model.QuizResult;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.QuestionService;
import nl.hva.election_backend.service.QuizResultService;
import nl.hva.election_backend.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> getQuiz() {
        return ResponseEntity.ok(quizService.getQuiz());
    }

    @PostMapping("/result")
    public ResponseEntity<?> submitQuiz(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> answers
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Geen geldige Authorization header."));
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.validateTokenAndGetEmail(token);

            QuizResult result = quizResultService.processQuiz(answers, email);
            // 201 CREATED omdat er iets nieuws is opgeslagen
            return ResponseEntity.status(201).body(result);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token verlopen → 401 Unauthorized
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Token is verlopen. Log opnieuw in."));
        } catch (Exception e) {
            // Andere fout → 400 Bad Request
            return ResponseEntity.status(400)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Haalt alle vragen op 200 OK.
    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    // Haalt één vraag op. Als niet gevonden → 404.
    @GetMapping("/questions/{id}")
    public ResponseEntity<?> getQuestionById(@PathVariable String id) {
        Question q = questionService.getQuestionById(id);

        if (q == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Vraag niet gevonden"));
        }
        return ResponseEntity.ok(q);
    }
}