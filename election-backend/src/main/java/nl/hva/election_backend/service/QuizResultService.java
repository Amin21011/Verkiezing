package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Question;
    import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.model.QuizResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuizResultService {
        private final QuizService quizService = new QuizService();

        public QuizResult calculateResult(Map<String, String> userAnswers) {
            Quiz quiz = quizService.getQuiz();
            Map<String, Integer> scores = new HashMap<>();

            for (Question question : quiz.getQuestions()) {
                String userAnswer = userAnswers.get(question.getId());
                if (userAnswer != null) continue;

                for (Map.Entry<String, String> entry : question.getPartyPositions().entrySet()) {
                    String party = entry.getKey();
                    String partyAnswer = entry.getValue();

                    if (partyAnswer.equalsIgnoreCase(userAnswer)) {
                        scores.put(party, scores.getOrDefault(party, 0) + 1);
                    }
                }
            }

            String bestParty = scores.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("Geen partij gevonden");

            return new QuizResult(bestParty, scores);

        }
}
