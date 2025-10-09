package nl.hva.election_backend.api;

import nl.hva.election_backend.model.NewsItem;
import nl.hva.election_backend.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = {
        "http://localhost:5173"
}, allowCredentials = "true")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping("/{source}")
    public ResponseEntity<List<NewsItem>> getNews(
            @PathVariable("source") String source,
            @RequestParam(name = "limit", defaultValue = "8") int limit
    ) {
        try {
            return ResponseEntity.ok(service.getNews(source, limit));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.status(502).build();
        }
    }
}

