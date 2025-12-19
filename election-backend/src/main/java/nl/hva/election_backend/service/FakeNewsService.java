package nl.hva.election_backend.service;
import nl.hva.election_backend.dto.AIResult;
import nl.hva.election_backend.dto.FakeNewsRequest;
import nl.hva.election_backend.dto.FakeNewsResponse;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FakeNewsService {
    private final FakeNewsAiService aiService;
    private static final List<String> POPULIST_KEYWORDS = List.of(
            "schandalig", "corrupt", "leugen", "oplichterij", "elite", "propaganda",
            "belachelijk", "NEP", "WAARHEID", "EXPOSED"
    );

    public FakeNewsService(FakeNewsAiService aiService) {
        this.aiService = aiService;
    }

    public FakeNewsResponse analyze(FakeNewsRequest req) {
        String article = extractArticleText(req);

        int sentimentScore = detectSentiment(article);
        int biasScore = detectBias(article);
        int sourceScore = detectSourceReliability(req.getUrl());

        List<String> keywordWarnings = detectKeywords(article);
        int capsIntensity = detectCapsIntensity(article);

        int nlpScore = (int) (
                biasScore * 0.35 +
                        capsIntensity * 0.25 +
                        sentimentScore * 0.20 +
                        (keywordWarnings.size() * 10) +
                        (100 - sourceScore) * 0.20
        );
        nlpScore = Math.min(100, nlpScore);


        AIResult ai = aiService.analyzeTextWithAIModels(article);

        int fakeScore = ai.getFakeNewsScore();
        Map<String, Double> ml = ai.getMultiLabel();


        int finalScore = (int) (nlpScore * 0.5 + fakeScore * 0.5);
        finalScore = Math.min(100, finalScore);

        return new FakeNewsResponse(
                finalScore,
                sentimentScoreToLabel(sentimentScore),
                biasScore,
                sourceScore,
                keywordWarnings,
                fakeScore,
                ml
        );
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

    private int detectCapsIntensity(String text) {
        long caps = text.chars().filter(Character::isUpperCase).count();
        float ratio = (caps / Math.max(text.length(), 1f)) * 100f;
        return Math.min(100, (int) ratio * 3);
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
