package nl.hva.election_backend.service;
import nl.hva.election_backend.helpers.ResourceNotFoundException;
import nl.hva.election_backend.helpers.ValidationException;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(String name, String email, String password) {
        if (repository.findByEmail(email).isPresent()) {
            throw new ValidationException("Dit e-mailadres is al in gebruik.");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        return repository.save(user);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Geen gebruiker gevonden met e-mailadres: " + email)
                );
    }

    public User save(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("E-mail mag niet leeg zijn.");
        }
        return repository.save(user);
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = findByEmail(email);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ValidationException("Oude wachtwoord is onjuist.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    public void updateBirthDate(String email, LocalDate birthDate) {
        User user = findByEmail(email);
        user.setBirthDate(birthDate);
        repository.save(user);
    }

    public void deleteUser(String email) {
        User user = findByEmail(email);
        repository.delete(user);
    }

    public void forceChangePassword(String email, String newPassword) {
        User user = findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
    }
}