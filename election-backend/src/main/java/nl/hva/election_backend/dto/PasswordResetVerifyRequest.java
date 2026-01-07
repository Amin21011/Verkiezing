package nl.hva.election_backend.dto;

import java.time.LocalDate;

public class PasswordResetVerifyRequest {
    private String email;
    private LocalDate birthDate;

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}