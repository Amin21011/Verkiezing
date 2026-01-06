package nl.hva.election_backend.api;

import nl.hva.election_backend.dto.PasswordChangeRequest;
import nl.hva.election_backend.dto.model.UserDTO;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.service.AuthService;
import nl.hva.election_backend.service.UserService;
import nl.hva.election_backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthService authService, JwtUtil jwtUtil, UserService userService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User savedUser = authService.register(user.getName(), user.getEmail(), user.getPassword());
        String token = jwtUtil.generateToken(savedUser);


        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", savedUser.getEmail(),
                "name", savedUser.getName(),
                "role", savedUser.getRole()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String token = authService.authenticate(email, password);
        User user = userService.findByEmail(email);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "email", user.getEmail(),
                "name", user.getName(),
                "role", user.getRole()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Geen geldige token gevonden"));
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(UserDTO.from(user));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UserDTO updatedUser
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Geen geldige token gevonden");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);
        User user = userService.findByEmail(email);
        user.setName(updatedUser.name());
        user.setEmail(updatedUser.email());
        User saved = userService.save(user);
        return ResponseEntity.ok(UserDTO.from(saved));
    }

    @PutMapping("/birthdate")
    public ResponseEntity<?> updateBirthDate(
            @RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody Map<String, String> body) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Geen geldige token gevonden"));
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        if (!body.containsKey("birthDate")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "birthDate is verplicht"));
        }

        LocalDate birthDate = LocalDate.parse(body.get("birthDate"));
        userService.updateBirthDate(email, birthDate);
        return ResponseEntity.ok(Map.of("message", "Geboortedatum opgeslagen"));
    }

    @DeleteMapping("/account")
    public ResponseEntity<?> deleteOwnAccount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);
        userService.deleteUser(email);
        return ResponseEntity.ok(Map.of("message", "Account verwijderd"));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader, @RequestBody PasswordChangeRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Geen geldige token gevonden"));
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        if (request.getOldPassword() == null || request.getNewPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Beide wachtwoordvelden zijn verplicht."));
        }

        try {
            userService.changePassword(email, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Wachtwoord succesvol gewijzigd"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
