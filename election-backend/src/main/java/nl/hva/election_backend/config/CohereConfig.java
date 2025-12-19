package nl.hva.election_backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CohereConfig {

    @Bean
    public WebClient cohereWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.cohere.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
