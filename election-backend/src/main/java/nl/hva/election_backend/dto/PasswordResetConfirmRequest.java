package nl.hva.election_backend.dto;

public class PasswordResetConfirmRequest {
    private String email;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public String getNewPassword() {
        return newPassword;
    }
}