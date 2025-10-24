package nl.hva.election_backend.controller;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.service.UserService;
import nl.hva.election_backend.security.JwtUtil;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");
        return userService.registerUser(name, email, password);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden"));

        if (!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                .matches(password, user.getPassword())) {
            throw new RuntimeException("Onjuist wachtwoord");
        }

        String token = jwtUtil.generateToken(email);
        return Map.of("token", token, "email", email);
    }
}
