package nl.hva.election_backend.dto.model;

public record UserDTO(Long id, String name, String email, String role, String quizBestMatch) {}
