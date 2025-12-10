package nl.hva.election_backend.dto.model;

public record PartyDTO(
        String id,
        String name,
        int voteCount
) {}
