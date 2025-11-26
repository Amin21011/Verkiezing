package nl.hva.election_backend.service;

import nl.hva.election_backend.dto.SeatSimulationRequest;
import nl.hva.election_backend.dto.SeatSimulationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatSimulationService {

    private final ResultService resultService;

    public SeatSimulationService(ResultService resultService) {
        this.resultService = resultService;
    }

    public SeatSimulationResponse simulate(SeatSimulationRequest request) {
        var totals = resultService.getVotesByParty();
        double factor = request.turnout() / 100.0;
        totals.replaceAll((party, votes) -> (int) (votes * factor));

        var seatMap = CalculatorHelper.calculate(totals, 150, request.threshold());

        List<SeatSimulationResponse.PartySeats> list =
                seatMap.entrySet().stream()
                        .map(e -> new SeatSimulationResponse.PartySeats(e.getKey(), e.getValue()))
                        .toList();

        int totalVotes = totals.values().stream().mapToInt(Integer::intValue).sum();
        int votesPerSeat = totalVotes / 150;

        return new SeatSimulationResponse(list, votesPerSeat);
    }
}
