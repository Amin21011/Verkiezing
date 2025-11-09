package nl.hva.election_backend.service;

import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.UserRepository;
import nl.hva.election_backend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserService userService;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil,  UserService userService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Geen gebruiker gevonden met dit e-mailadres."));

        if (!encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Het ingevoerde wachtwoord is onjuist.");
        }

        return jwtUtil.generateToken(user);
    }

    public User register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Dit e-mailadres is al geregistreerd.");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Het wachtwoord moet minimaal 8 tekens lang zijn.");
        }

        return userService.registerUser(name, email, password);
    }
}
