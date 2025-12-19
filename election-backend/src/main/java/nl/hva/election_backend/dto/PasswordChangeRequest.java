package nl.hva.election_backend.dto;

public class PasswordChangeRequest {
    private String oldPassword;
    private String newPassword;

    public PasswordChangeRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
