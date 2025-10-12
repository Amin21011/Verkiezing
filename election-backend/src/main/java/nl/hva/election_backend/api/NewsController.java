package nl.hva.election_backend.api;

import nl.hva.election_backend.model.NewsItem;
import nl.hva.election_backend.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<NewsItem> getNews(@RequestParam String url) {
        return newsService.fetch(url);
    }

}

