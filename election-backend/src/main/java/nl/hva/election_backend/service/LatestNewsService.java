package nl.hva.election_backend.service;

import nl.hva.election_backend.model.LatestNews;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LatestNewsService {
    public List<LatestNews> getLatestNews() {
        return List.of(
                new LatestNews("Verkiezingsdebat aangekondigd", "samenvatting", "7 okt 2025"),
                new LatestNews("Verkiezingsdebat aangekondigd", "samenvatting", "7 okt 2025"),
                new LatestNews("Verkiezingsdebat aangekondigd", "samenvatting", "7 okt 2025"),
                new LatestNews("Welke partij heeft nou de meeste zetels?", "samenvatting","9 okt 2025")
        );
    }
}
