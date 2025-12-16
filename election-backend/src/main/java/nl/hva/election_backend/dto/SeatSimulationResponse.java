package nl.hva.election_backend.dto;

import java.util.List;

public record SeatSimulationResponse(List<PartySeats> seats, int votesPerSeat) {
    public record PartySeats(String party, int seats) {}
}
