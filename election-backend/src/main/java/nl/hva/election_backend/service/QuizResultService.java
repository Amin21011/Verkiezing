package nl.hva.election_backend.service;

import nl.hva.election_backend.model.*;
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

    private static final Map<String, String> PARTY_NAME_TO_ID = Map.ofEntries(
            Map.entry("VVD", "vvd"),
            Map.entry("GroenLinks-PvdA", "groenlinks___partij_van_de_arbeid_(pvda)"),
            Map.entry("D66", "d66"),
            Map.entry("PVV", "pvv_(partij_voor_de_vrijheid)"),
            Map.entry("CDA", "cda"),
            Map.entry("FvD", "forum_voor_democratie"),
            Map.entry("BIJ1", "bij1"),
            Map.entry("PvdD", "partij_voor_de_dieren"),
            Map.entry("ChristenUnie", "christenunie"),
            Map.entry("SP", "sp_(socialistische_partij)"),
            Map.entry("50Plus", "50plus"),
            Map.entry("DENK", "denk"),
            Map.entry("FNP", "fnp"),
            Map.entry("Vrede voor Dieren", "vrede_voor_dieren"),
            Map.entry("JA21", "ja21"),
            Map.entry("Volt", "volt"),
            Map.entry("SGP", "staatkundig_gereformeerde_partij_(sgp)"),
            Map.entry("BBB", "bbb"),
            Map.entry("NSC", "nieuw_sociaal_contract"),
            Map.entry("BVNL", "bvnl___groep_van_haga"),
            Map.entry("LP", "lp_(libertaire_partij)"),
            Map.entry("PiratenPartij", "piratenpartij___de_groenen"),
            Map.entry("Vrij Verbond", "vrij_verbond"),
            Map.entry("De Linie", "de_linie")
    );

    public QuizResult processQuiz(Map<String, String> userAnswers, String email) {
        QuizResult result = calculateResult(userAnswers);
        saveQuizResult(email, result.getBestMatchingParty());
        return result;
    }

    public QuizResult calculateResult(Map<String, String> userAnswers) {

        Quiz quiz = quizService.getQuiz();
        Map<String, Double> scores = new HashMap<>();
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            return new QuizResult("Geen vragen", Map.of());
        }

        for (Question question : quiz.getQuestions()) {
            String userAnswer = userAnswers.get(String.valueOf(question.getId()));
            if (userAnswer == null) continue;

            if (question.getPartyPositions() == null || question.getPartyPositions().isEmpty()) {
                continue;
            }

            for (PartyPosition partyPosition : question.getPartyPositions()) {

                if (partyPosition == null) continue;
                if (partyPosition.getParty() == null) continue;
                if (partyPosition.getPosition() == null) continue;

                String partyName = partyPosition.getParty();
                String party = PARTY_NAME_TO_ID.getOrDefault(partyName, partyName);
                String partyAnswer = partyPosition.getPosition().name();
                String normalizedUserAnswer = userAnswer.toUpperCase();

                if (partyAnswer.equals(normalizedUserAnswer)) {
                    scores.put(party, scores.getOrDefault(party, 0.0) + 1.0);
                } else if (
                        partyAnswer.equals("NEUTRAAL") ||
                                normalizedUserAnswer.equals("NEUTRAAL")
                ) {
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

        String bestMatchingParty = percentages.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Geen partij gevonden");

        return new QuizResult(bestMatchingParty, percentages);
    }

    public void saveQuizResult(String email, String bestMatchingParty) {
        User user = userService.findByEmail(email);
        user.setQuizBestMatch(bestMatchingParty);
        userService.save(user);
    }
}
