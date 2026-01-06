package nl.hva.election_backend.service;
import nl.hva.election_backend.dto.AIResult;
import nl.hva.election_backend.dto.FakeNewsRequest;
import nl.hva.election_backend.dto.model.FakeNewsResponse;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FakeNewsService {
    private final FakeNewsAiService aiService;
    private static final List<String> POPULIST_KEYWORDS = List.of(
            "schandalig", "corrupt", "leugen", "oplichterij", "elite", "propaganda",
            "belachelijk", "NEP", "WAARHEID", "EXPOSED", "TRUTH", "FAKE"
    );

    public FakeNewsService(FakeNewsAiService aiService) {
        this.aiService = aiService;
    }

    public FakeNewsResponse analyze(FakeNewsRequest req) {
        String article = extractArticleText(req);
        int sentimentScore = detectSentiment(article);
        int biasScore = detectBias(article);

        AIResult ai = aiService.analyzeTextWithAIModels(article);
        Map<String, Double> ml = ai.getMultiLabel();

        double weightedAiScore = (ml.getOrDefault("disinformation", 0.0) * 0.6) +
                (ml.getOrDefault("clickbait", 0.0) * 0.4);

        int finalScore = calculateFinalRiskScore(biasScore, sentimentScore, (int)(weightedAiScore * 100));
        String verdict = generateHumanVerdict(finalScore, ml);

        return new FakeNewsResponse(
                finalScore,
                sentimentScoreToLabel(sentimentScore),
                biasScore,
                detectSourceReliability(req.getUrl()),
                detectKeywords(article),
                ai.getFakeNewsScore(),
                ml,
                verdict
        );
    }

    private int calculateFinalRiskScore(int bias, int sentiment, int aiScore) {
        // Formule: AI weegt voor 60% mee, Bias voor 20%, Sentiment voor 20%
        double total = (aiScore * 0.6) + (bias * 0.2) + (sentiment * 0.2);
        return (int) Math.min(100, Math.max(0, total));
    }

    private String generateHumanVerdict(int score, Map<String, Double> ml) {
        if (score > 75) return "KRITIEK: Dit artikel vertoont sterke kenmerken van gecoördineerde desinformatie.";
        if (score > 50) return "WAARSCHUWING: Sterke clickbait of subjectieve bias gedetecteerde. Controleer feiten.";
        if (ml.getOrDefault("clickbait", 0.0) > 0.7) return "OPMERKING: De inhoud lijkt feitelijk, maar de kop is misleidend (Clickbait).";
        return "GELOOFWAARDIG: Geen significante manipulatieve patronen gevonden.";
    }

    private String extractArticleText(FakeNewsRequest req) {
        try {
            if (req.getUrl() != null && !req.getUrl().isBlank()) {
                return Jsoup.connect(req.getUrl())
                        .userAgent("Mozilla")
                        .get()
                        .text();
            }
        } catch (Exception ignored) {}
        return Optional.ofNullable(req.getText()).orElse("");
    }

    private int detectSentiment(String text) {
        if (text.isBlank()) return 0;
        int hits = text.toLowerCase().split(" slecht | gevaar | crisis | boos | woedend | nep ").length - 1;
        return Math.min(100, hits * 15);
    }

    private int detectBias(String text) {
        int hits = 0;
        if (text.contains("!!!")) hits += 30;
        if (text.toLowerCase().contains("ze willen dat je dit niet weet")) hits += 50;
        return Math.min(100, hits);
    }

    private List<String> detectKeywords(String text) {
        List<String> found = new ArrayList<>();
        for (String k : POPULIST_KEYWORDS) {
            if (text.toLowerCase().contains(k.toLowerCase())) {
                found.add(k);
            }
        }
        return found;
    }

    private int detectSourceReliability(String url) {
        if (url == null) return 50;
        if (url.contains("nos.nl") || url.contains("bbc") || url.contains("nytimes")) return 80;
        if (url.contains("blogspot") || url.contains("telegram") || url.contains("truthnews")) return 20;
        return 50;
    }

    private String sentimentScoreToLabel(int score) {
        if (score > 70) return "Sterk negatief";
        if (score > 40) return "Negatief";
        if (score > 20) return "Neutraal";
        return "Licht negatief";
    }
}
