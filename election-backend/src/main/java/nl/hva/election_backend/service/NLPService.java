package nl.hva.election_backend.service;
import nl.hva.election_backend.dto.AIResult;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class NLPService {
    public AIResult analyze(String text) {
        int fake = 0;
        if (text.contains("!!!")) fake += 20;
        if (text.contains("NEP") || text.contains("fake")) fake += 30;
        if (text.contains("WAARHEID WORDT VERZWEGEN")) fake = 90;
        if (text.length() < 120) fake += 20;

        fake = Math.min(100, fake);

        return new AIResult(fake, Map.of(
                "fake", fake / 100.0,
                "real", 1 - (fake / 100.0)
        ));
    }
}
