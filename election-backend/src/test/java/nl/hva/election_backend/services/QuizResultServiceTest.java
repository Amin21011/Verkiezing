package nl.hva.election_backend.services;

import nl.hva.election_backend.model.*;
import nl.hva.election_backend.service.QuizResultService;
import nl.hva.election_backend.service.QuizService;
import nl.hva.election_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Activeert Mockito
class QuizResultServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private QuizService quizService;
    private QuizResultService service;

    @BeforeEach
    void setup() {
        service = new QuizResultService(userService, quizService);
    }

    @Test
    void processQuiz_returnsCorrectResult_whenValidInput() {
        // Arrange: maak een quiz met 1 vraag en 1 partijpositie
        Question q = new Question();
        q.setId(1L);

        PartyPosition pos = new PartyPosition();
        pos.setParty("partij_voor_de_dieren");
        pos.setPosition(Position.JA);

        q.setPartyPositions(List.of(pos));

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(q));

        // Mock: quizService.getQuiz() retourneert nep quiz
        when(quizService.getQuiz()).thenReturn(quiz);

        // Mock: userService.findByEmail()
        User user = new User();
        user.setEmail("test@mail.com");
        when(userService.findByEmail("test@mail.com")).thenReturn(user);

        // Gebruiker antwoordt "JA"
        Map<String, String> answers = Map.of("1", "JA");

        // Act
        QuizResult result = service.processQuiz(answers, "test@mail.com");

        // Assert: beste partij moet PvdD zijn
        assertEquals("partij_voor_de_dieren", result.getBestMatchingParty());

        // Assert: percentage moet 100% zijn
        assertEquals(100.0, result.getPercentages().get("partij_voor_de_dieren"));

        // Assert: userService.save() moet zijn aangeroepen
        verify(userService, times(1)).save(user);
    }

    @Test
    void calculateResult_returnsGeenVragen_whenQuizHasNoQuestions() {
        // Arrange: quiz zonder vragen
        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of()); // lege lijst

        when(quizService.getQuiz()).thenReturn(quiz);

        // Act
        QuizResult result = service.calculateResult(Map.of());

        // Assert
        assertEquals("Geen vragen", result.getBestMatchingParty());
        assertTrue(result.getPercentages().isEmpty());
    }

    @Test
    void calculateResult_returnsGeenPartijGevonden_whenNoMatchingAnswers() {
        // Arrange: quiz met 1 vraag maar user geeft geen antwoord
        Question q = new Question();
        q.setId(1L);

        PartyPosition pos = new PartyPosition();
        pos.setParty("VVD");
        pos.setPosition(Position.JA);

        q.setPartyPositions(List.of(pos));

        Quiz quiz = new Quiz();
        quiz.setQuestions(List.of(q));

        when(quizService.getQuiz()).thenReturn(quiz);

        // Act
        QuizResult result = service.calculateResult(Map.of()); // geen antwoorden

        // Assert
        assertEquals("Geen partij gevonden", result.getBestMatchingParty());
    }
}

