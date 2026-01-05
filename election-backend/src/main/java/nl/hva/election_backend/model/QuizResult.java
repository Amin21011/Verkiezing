package nl.hva.election_backend.model;

import java.util.Map;

public class QuizResult {
    private final String bestMatchingParty;
    private final Map<String, Double> percentages;

    public QuizResult(String bestMatchingParty, Map<String, Double> percentages) {
        this.bestMatchingParty = bestMatchingParty;
        this.percentages = percentages;
    }

    public String getBestMatchingParty() {
        return bestMatchingParty;
    }
    public Map<String, Double> getPercentages() {
        return percentages;
    }
}
