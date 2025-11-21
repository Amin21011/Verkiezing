package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Question;
import nl.hva.election_backend.model.Quiz;
import nl.hva.election_backend.model.QuizResult;
import nl.hva.election_backend.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuizResultService {
    private final UserService userService;
    private final QuizService quizService;

    public QuizResultService(UserService userService, QuizService quizService) {
        this.userService = userService;
        this.quizService = quizService;
    }

    public QuizResult calculateResult(Map<String, String> userAnswers) {

        Quiz quiz = quizService.getQuiz();
        Map<String, Double> scores = new HashMap<>();

        for (Question question : quiz.getQuestions()) {
            String userAnswer = userAnswers.get(question.getId());
            if (userAnswer == null) continue;

            for (Map.Entry<String, String> entry : question.getPartyPositions().entrySet()) {

                String party = entry.getKey();
                String partyAnswer = entry.getValue();

                if (partyAnswer.equalsIgnoreCase(userAnswer)) {
                    scores.put(party, scores.getOrDefault(party, 0.0) + 1.0);

                } else if (partyAnswer.equalsIgnoreCase("Neutraal")
                        && userAnswer.equalsIgnoreCase("Neutraal")) {

                    scores.put(party, scores.getOrDefault(party, 0.0) + 1.0);

                } else if (partyAnswer.equalsIgnoreCase("Neutraal")
                        || userAnswer.equalsIgnoreCase("Neutraal")) {

                    scores.put(party, scores.getOrDefault(party, 0.0) + 0.5);
                }
            }
        }

        int totalQuestions = quiz.getQuestions().size();
        Map<String, Double> percentages = new HashMap<>();

        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            double percent = (entry.getValue() / totalQuestions) * 100;
            percentages.put(entry.getKey(), percent);
        }

        String bestParty = percentages.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Geen partij gevonden");

        return new QuizResult(bestParty, percentages);
    }

    public void saveQuizResult(String email, String bestParty) {
        User user = userService.findByEmail(email);
        user.setQuizCompleted(true);
        user.setQuizBestMatch(bestParty);
        userService.save(user);
    }
}
