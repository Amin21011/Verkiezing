package nl.hva.election_backend.dto;

import java.util.Map;

public class AIResult {
    public int fakeNewsScore;
    public Map<String, Double> multiLabel;

    public AIResult(int fakeNewsScore, Map<String, Double> multiLabel) {
        this.fakeNewsScore = fakeNewsScore;
        this.multiLabel = multiLabel;
    }

    public int getFakeNewsScore() { return fakeNewsScore; }
    public Map<String, Double> getMultiLabel() { return multiLabel; }
}
