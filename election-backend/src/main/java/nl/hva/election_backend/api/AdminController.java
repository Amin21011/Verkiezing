package nl.hva.election_backend.api;

import nl.hva.election_backend.dto.model.AdminDTO;
import nl.hva.election_backend.dto.model.UserDTO;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.UserRepository;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AdminController(UserRepository userRepository, UserService userService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    private User requireAdmin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Geen geldige token");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);
        User user = userService.findByEmail(email);

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Geen adminrechten");
        }
        return user;
    }

    @GetMapping("/users")
    public List<AdminDTO> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        requireAdmin(authHeader);
        return userRepository.findAll().stream()
                .map(AdminDTO::from)
                .toList();
    }

    @GetMapping("/admins")
    public List<AdminDTO> getAdmins(@RequestHeader("Authorization") String authHeader) {
        requireAdmin(authHeader);
        return userRepository.findAll().stream()
                .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
                .map(AdminDTO::from)
                .toList();
    }

    @PutMapping("/{id}/role")
    public UserDTO updateRole(@RequestHeader("Authorization") String authHeader, @PathVariable Long id, @RequestParam String role) {
        User admin = requireAdmin(authHeader);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden"));

        if (user.getId().equals(admin.getId())) {
            throw new RuntimeException("Je kunt je eigen rol niet aanpassen");
        }

        user.setRole(role.toUpperCase());
        userRepository.save(user);
        return UserDTO.from(user);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteUser(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        requireAdmin(authHeader);
        userRepository.deleteById(id);
        return Map.of("message", "Gebruiker verwijderd");
    }
}
