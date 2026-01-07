package nl.hva.election_backend.api.response;

public record AuthResponse(
        String token,
        String email,
        String name,
        String role
) implements ApiResponse {}