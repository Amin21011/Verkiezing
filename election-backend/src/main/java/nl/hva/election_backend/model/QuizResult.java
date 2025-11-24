package nl.hva.election_backend.model;

import java.util.Map;

public class QuizResult {
    private final String bestMatchingParty;
    private final Map<String, Double> partyScores;

    public QuizResult(String bestMatchingParty, Map<String, Double> partyScores) {
        this.bestMatchingParty = bestMatchingParty;
        this.partyScores = partyScores;
    }

    public String getBestMatchingParty() {
        return bestMatchingParty;
    }

    public String getBestMatch() {
        return bestMatchingParty;
    }

    public Map<String, Double> getPartyScores() {
        return partyScores;
    }
}
