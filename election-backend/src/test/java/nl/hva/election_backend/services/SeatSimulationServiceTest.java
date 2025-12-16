package nl.hva.election_backend.services;
import nl.hva.election_backend.dto.SeatSimulationRequest;
import nl.hva.election_backend.dto.SeatSimulationResponse;
import nl.hva.election_backend.service.ResultService;
import nl.hva.election_backend.service.SeatSimulationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatSimulationServiceTest {
    @Mock
    private ResultService resultService;

    @InjectMocks
    private SeatSimulationService seatSimulationService;

    @Test
    void simulate_appliesTurnoutThresholdAndCalculatesSeats() {
        Map<String, Integer> votes = new HashMap<>();
        votes.put("vvd", 2_000_000);
        votes.put("pvda", 1_500_000);
        votes.put("bbb", 200_000);

        when(resultService.getVotesByParty()).thenReturn(votes);
        SeatSimulationRequest request =
                new SeatSimulationRequest(80, 0.75);

        SeatSimulationResponse response =
                seatSimulationService.simulate(request);

        assertNotNull(response);
        assertFalse(response.seats().isEmpty());

        int totalSeats = response.seats()
                .stream()
                .mapToInt(SeatSimulationResponse.PartySeats::seats)
                .sum();

        assertEquals(150, totalSeats);
        assertTrue(response.votesPerSeat() > 0);
    }
}
