package nl.hva.election_backend.model;

import java.util.Map;

public class QuizResult {
    private final String bestMatchingParty;
    private final Map<String, Integer> partyScores;

    public QuizResult(String bestMatchingParty, Map<String, Integer> partyScores) {
        this.bestMatchingParty = bestMatchingParty;
        this.partyScores = partyScores;
    }

    public String getBestMatchingParty() {
        return bestMatchingParty;
    }

    public Map<String, Integer> getPartyScores() {
        return partyScores;
    }
}
