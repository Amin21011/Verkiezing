package nl.hva.election_backend.dto;

public record UserDTO(Long id, String name, String email, String role, String quizBestMatch) {}
