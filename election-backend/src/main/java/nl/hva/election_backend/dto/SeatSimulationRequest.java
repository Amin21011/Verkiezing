package nl.hva.election_backend.dto;

public record SeatSimulationRequest(
        double turnout,
        double threshold
) {}