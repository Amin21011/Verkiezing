package nl.hva.election_backend.dto.model;

public record DailyFactResponse(
        String type,
        String title,
        String description,
        String value
) {}
