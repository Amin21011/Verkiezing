package nl.hva.election_backend.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {
    private final Map<String, Long> allowedResets = new ConcurrentHashMap<>();
    private static final long TTL_MS = 10 * 60 * 1000;

    public void allowReset(String email) {
        allowedResets.put(email, System.currentTimeMillis());
    }

    public boolean isResetAllowed(String email) {
        Long timestamp = allowedResets.get(email);

        if (timestamp == null) return false;

        if (System.currentTimeMillis() - timestamp > TTL_MS) {
            allowedResets.remove(email);
            return false;
        }

        return true;
    }

    public void clear(String email) {
        allowedResets.remove(email);
    }
}
