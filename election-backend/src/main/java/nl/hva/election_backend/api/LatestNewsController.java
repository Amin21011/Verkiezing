package nl.hva.election_backend.api;

import nl.hva.election_backend.model.LatestNews;
import nl.hva.election_backend.service.LatestNewsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/latest-news")
@RestController
@CrossOrigin(origins = { "http://localhost:5173" })
public class LatestNewsController {
    private final LatestNewsService latestNewsService;

    public LatestNewsController(LatestNewsService latestNewsService) {
        this.latestNewsService = latestNewsService;
    }

    @GetMapping
    public List<LatestNews> getLatestNews() {
        return latestNewsService.getLatestNews();
    }

}
