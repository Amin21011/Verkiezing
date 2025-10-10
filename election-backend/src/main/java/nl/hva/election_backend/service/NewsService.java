package nl.hva.election_backend.service;

import nl.hva.election_backend.model.NewsItem;
import nl.hva.election_backend.utils.xml.RssParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final RssClient client;
    private final RssParser parser;

    public NewsService(RssClient client, RssParser parser) {
        this.client = client;
        this.parser = parser;
    }

    private static final Map<String, String> ALLOWED_FEEDS = Map.of(
            "rijksoverheid", "https://feeds.rijksoverheid.nl/regering/nieuws.rss"
    );

    private final Map<String, CacheEntry> cache = new HashMap<>();
    private static final long TTL_MILLIS = 60_000; // 60s

    public List<NewsItem> getNews(String sourceKey, int limit) {
        String url = ALLOWED_FEEDS.get(sourceKey);
        if (url == null) {
            throw new IllegalArgumentException("Onbekende nieuwsbron: " + sourceKey);
        }

        List<NewsItem> items = getCachedOrFetch(sourceKey, url);


        List<NewsItem> sorted = items.stream()
                .sorted((a, b) -> {
                    if (a.getPublishedAt() == null && b.getPublishedAt() == null) return 0;
                    if (a.getPublishedAt() == null) return 1;
                    if (b.getPublishedAt() == null) return -1;
                    return b.getPublishedAt().compareTo(a.getPublishedAt());
                })
                .collect(Collectors.toList());

        if (limit > 0 && limit < sorted.size()) {
            return sorted.subList(0, limit);
        }
        return sorted;
    }

    private List<NewsItem> getCachedOrFetch(String sourceKey, String url) {
        CacheEntry ce = cache.get(sourceKey);
        long now = System.currentTimeMillis();

        if (ce != null && (now - ce.cachedAt) < TTL_MILLIS) {
            return ce.items;
        }

        String xml = client.fetch(url);
        List<NewsItem> parsed = parser.parse(xml);
        cache.put(sourceKey, new CacheEntry(parsed, now));
        return parsed;
    }

    private static class CacheEntry {
        final List<NewsItem> items;
        final long cachedAt;
        CacheEntry(List<NewsItem> items, long cachedAt) {
            this.items = items;
            this.cachedAt = cachedAt;
        }
    }
}