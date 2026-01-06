package nl.hva.election_backend;

import nl.hva.election_backend.model.Candidate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CandidateControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllCandidates() {
        ResponseEntity<Candidate[]> response = restTemplate.getForEntity("/api/candidates", Candidate[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    void testGetCandidatesByParty() {
        String partyId = "1"; // een bestaande partyId in je testdata
        ResponseEntity<Candidate[]> response = restTemplate.getForEntity("/api/candidates/party/" + partyId, Candidate[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).allMatch(c -> c.getPartyId().equals(partyId));
    }

    @Test
    void testCompareCandidates() {
        List<Map<String, String>> payload = List.of(
                Map.of("candidateId", "1", "partyId", "1"),
                Map.of("candidateId", "2", "partyId", "2")
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Map<String, String>>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Candidate[]> response = restTemplate.postForEntity("/api/candidates/compare", request, Candidate[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(2);
    }
}
