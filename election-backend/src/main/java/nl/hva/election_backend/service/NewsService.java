package nl.hva.election_backend.service;

import nl.hva.election_backend.model.NewsItem;
import nl.hva.election_backend.utils.xml.NewsParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NewsService {

    private final NewsParser newsParser = new NewsParser();
    private final RestTemplate restTemplate = new RestTemplate();

    public List<NewsItem> fetch(String feedUrl) {
        String xml = restTemplate.getForObject(feedUrl, String.class);
        return  newsParser.parse(xml);
    }

}