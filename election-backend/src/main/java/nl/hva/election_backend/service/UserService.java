package nl.hva.election_backend.service;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private static UserRepository repository = null;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository) {
        UserService.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(String name, String email, String password) {
        if (repository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is al in gebruik");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
