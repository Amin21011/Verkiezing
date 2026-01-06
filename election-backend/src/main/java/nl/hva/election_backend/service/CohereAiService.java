package nl.hva.election_backend.service;

import nl.hva.election_backend.dto.AIResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Service
public class CohereAiService {
    @Value("${cohere.api.key:}")
    private String apiKey;

    private final WebClient cohere;

    public CohereAiService(WebClient cohereWebClient) {
        this.cohere = cohereWebClient;
    }

    public AIResult analyze(String text) {
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }

        try {
            Map<String, Object> body = Map.of(
                    "model", "command-light",
                    "inputs", List.of(text),
                    "examples", List.of(),
                    "labels", List.of("fake", "real", "biased", "clickbait", "misleading")
            );

            Map resp = cohere.post()
                    .uri("/v1/classify")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> classifications = (List<Map<String, Object>>) resp.get("classifications");
            Map<String, Object> output = classifications.get(0);
            Map<String, Double> confidences = (Map<String, Double>) output.get("confidence");

            double fake = confidences.getOrDefault("fake", 0.0);

            return new AIResult((int) (fake * 100), confidences);

        } catch (Exception e) {
            return null;
        }
    }
}
