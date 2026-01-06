package nl.hva.election_backend.dto.model;

public record SeatSimulationRequest(
        double turnout,
        double threshold
) {}