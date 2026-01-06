package nl.hva.election_backend.dto.model;
import nl.hva.election_backend.model.User;

public record AdminDTO(
        Long id,
        String name,
        String email,
        String role,
        String quizBestMatch
) {
    public static AdminDTO from(User u) {
        return new AdminDTO(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getRole(),
                u.getQuizBestMatch()
        );
    }
}