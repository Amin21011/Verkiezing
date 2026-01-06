package nl.hva.election_backend.dto.model;

import java.util.List;
import java.util.Map;

public record FakeNewsResponse(
        int score,
        String sentiment,
        int bias,
        int sourceReliability,
        List<String> keywords,
        int fakeNewsScore,
        Map<String, Double> multiLabelScores,
        String verdict) {
}
