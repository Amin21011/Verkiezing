package nl.hva.election_backend.helpers;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    protected final HttpStatus status;

    protected ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}