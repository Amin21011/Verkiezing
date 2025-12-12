package nl.hva.election_backend.dto.model;

public record CandidateDTO(
        String id,
        String firstName,
        String lastName,
        String partyId,
        String partyName,
        int votes
) {}