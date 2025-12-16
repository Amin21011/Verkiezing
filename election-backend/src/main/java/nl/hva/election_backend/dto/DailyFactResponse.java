package nl.hva.election_backend.dto;

public record DailyFactResponse(
        String type,
        String title,
        String description,
        String value
) {}
