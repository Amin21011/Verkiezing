package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.model.Question;

import java.util.List;

public class QuizService {

    public Quiz getQuiz() {
        // Dummyvragen
        Question q1 = new Question(
                "q1",
                "Moeten de belastingen voor de rijken omhoog?",
                List.of("Ja", "Nee", "Neutraal")
        );

        Question q2 = new Question(
                "q2",
                "Moeten scholen gratis zijn?",
                List.of("Ja", "Nee")
        );

        Question q3 = new Question(
                "q3",
                "Moet er meer geld naar de gezondheidszorg?",
                List.of("Ja", "Nee", "Weet ik niet")
        );

        return new Quiz(List.of(q1, q2, q3));
    }
}