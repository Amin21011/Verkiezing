package nl.hva.election_backend.api.response;

public record ErrorResponse(String error) implements ApiResponse {
}