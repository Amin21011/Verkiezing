package nl.hva.election_backend.dto.model;

public record ResultDTO(
        String partyName,
        int votes
) {}