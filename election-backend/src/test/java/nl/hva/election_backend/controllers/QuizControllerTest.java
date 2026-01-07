package nl.hva.election_backend.controllers;

import nl.hva.election_backend.api.QuizController;
import nl.hva.election_backend.model.QuizResult;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.QuestionService;
import nl.hva.election_backend.service.QuizResultService;
import nl.hva.election_backend.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

    @Mock
    private QuizService quizService;
    @Mock
    private QuizResultService quizResultService;
    @Mock
    private QuestionService questionService;
    @Mock
    private JwtUtil jwtUtil;
    private QuizController controller;

    @BeforeEach
    void setup() {
        controller = new QuizController(quizService, quizResultService, jwtUtil, questionService);
    }

    @Test
    void submitQuiz_returnsResult_whenValidToken() {
        Map<String, String> answers = Map.of("q1", "Ja");

        // Nep quizresultaat dat de service zou teruggeven
        QuizResult mockResult = new QuizResult(
                "partij_voor_de_dieren",
                Map.of("partij_voor_de_dieren", 80.0)
        );

        // Mock gedrag: token is geldig → email wordt teruggegeven
        when(jwtUtil.validateTokenAndGetEmail("validtoken")).thenReturn("test@mail.com");

        // Mock gedrag: service verwerkt quiz en geeft resultaat terug
        when(quizResultService.processQuiz(answers, "test@mail.com")).thenReturn(mockResult);

        // Act: voer de controller-methode uit
        ResponseEntity<?> response = controller.submitQuiz("Bearer validtoken", answers);

        // Assert: controleer statuscode + body
        assertEquals(201, response.getStatusCode().value());
        assertEquals(mockResult, response.getBody());
    }

    @Test
    void submitQuiz_returns401_whenMissingAuthHeader() {
        // Act: roep controller aan zonder Authorization header
        ResponseEntity<?> response = controller.submitQuiz(null, Map.of());

        // Assert: controller moet 401 Unauthorized teruggeven
        assertEquals(401, response.getStatusCode().value());
        assertEquals(
                Map.of("error", "Geen geldige Authorization header."),
                response.getBody()
        );
    }

    @Test
    void submitQuiz_returns401_whenInvalidAuthHeader() {
        // Act: header bestaat wel, maar is ongeldig (mist 'Bearer ')
        ResponseEntity<?> response = controller.submitQuiz("Token 123", Map.of());

        // Assert: controller moet opnieuw 401 Unauthorized teruggeven
        assertEquals(401, response.getStatusCode().value());
        assertEquals(
                Map.of("error", "Geen geldige Authorization header."),
                response.getBody()
        );
    }
}

