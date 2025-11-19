package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.utils.xml.QuizParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    private final QuizParser quizParser = new QuizParser();

    public Quiz getQuiz() {
        try {
            Quiz quiz = quizParser.parseQuiz("/quiz.xml");
            logger.info("Quiz geladen met {} vragen", quiz.getQuestions().size());
            return quiz;
        } catch (Exception e) {
            logger.error("Fout bij het laden van de quiz", e);
            throw new RuntimeException("Fout bij het laden van de quiz: " + e.getMessage(), e);
        }
    }
}
