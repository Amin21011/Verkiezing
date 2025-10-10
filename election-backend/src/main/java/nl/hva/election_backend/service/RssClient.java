package nl.hva.election_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RssClient {
    private final RestTemplate http = new RestTemplate();

    public String fetch(String url) {
        try {
            ResponseEntity<String> res = http.getForEntity(url, String.class);


            if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
                throw new RuntimeException("RSS HTTP status " + res.getStatusCode());
            }

            return res.getBody();
        } catch (Exception e) {

            throw new RuntimeException("Kon RSS niet ophalen: " + url, e);
        }
    }
}
