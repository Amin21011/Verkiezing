package nl.hva.election_backend.dto.model;

public record BirthDateRequest(
        String email,
        String birthDate
) {}