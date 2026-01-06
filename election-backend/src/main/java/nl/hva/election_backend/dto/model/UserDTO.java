package nl.hva.election_backend.dto.model;
import nl.hva.election_backend.model.User;
import java.time.LocalDate;

public record UserDTO(
        Long id,
        String name,
        String email,
        String role,
        String quizBestMatch,
        LocalDate birthDate,
        LocalDate createdAt
) {
    public static UserDTO from(User u) {
        return new UserDTO(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getRole(),
                u.getQuizBestMatch(),
                u.getBirthDate(),
                u.getCreatedAt()
        );
    }
}