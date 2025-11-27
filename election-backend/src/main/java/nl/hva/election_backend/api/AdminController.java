package nl.hva.election_backend.api;

import nl.hva.election_backend.dto.UserDTO;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.UserRepository;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/auth/admin")
public class AdminController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AdminController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private ResponseEntity<?> validateAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Geen geldige token gevonden"));
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.validateTokenAndGetEmail(token);
            User user = userService.findByEmail(email);

            if (!"ADMIN".equals(user.getRole())) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "Geen toegang — alleen admin toegestaan"));
            }
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Token ongeldig of verlopen"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllNormalUsers(@RequestHeader("Authorization") String authHeader) {
        ResponseEntity<?> authCheck = validateAdmin(authHeader);
        if (authCheck != null) return authCheck;

        List<User> users = userRepository.findAll();
        List<UserDTO> result = users.stream()
                .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getRole(),
                        u.getQuizBestMatch()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        ResponseEntity<?> authCheck = validateAdmin(authHeader);
        if (authCheck != null) return authCheck;

        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Gebruiker niet gevonden"));
        }
        userRepository.delete(userOpt.get());

        return ResponseEntity.ok(
                Map.of("message", "Gebruiker succesvol verwijderd")
        );
    }

    @PutMapping ("/{id}/role")
    public ResponseEntity<?> updateRole(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam String role) {

        ResponseEntity<?> authCheck = validateAdmin(authHeader);
        if (authCheck != null) return authCheck;

        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Gebruiker niet gevonden"));
        }

        if (!List.of("USER", "ADMIN").contains(role.toUpperCase())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ongeldige rol. Gebruik: USER, EDITOR, ADMIN"));
        }

        User user = userOpt.get();
        user.setRole(role.toUpperCase());
        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Rol succesvol aangepast",
                        "email", user.getEmail(),
                        "role", user.getRole()
                )
        );
    }
}
