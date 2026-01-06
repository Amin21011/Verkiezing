package nl.hva.election_backend.service;

import nl.hva.election_backend.dto.AIResult;
import org.springframework.stereotype.Service;

@Service
public class FakeNewsAiService {
    private final CohereAiService cohere;
    private final NLPService fallback;

    public FakeNewsAiService(CohereAiService cohere, NLPService fallback) {
        this.cohere = cohere;
        this.fallback = fallback;
    }

    public AIResult analyzeTextWithAIModels(String text) {
        AIResult ai = cohere.analyze(text);
        if (ai != null) {
            return ai;
        }

        return fallback.analyze(text);
    }
}
