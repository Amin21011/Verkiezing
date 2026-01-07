package nl.hva.election_backend.api;
import nl.hva.election_backend.api.response.AuthResponse;
import nl.hva.election_backend.api.response.MessageResponse;
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
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        User savedUser = authService.register(user.getName(), user.getEmail(), user.getPassword());
        String token = jwtUtil.generateToken(savedUser);

        return ResponseEntity.ok(
                new AuthResponse(token, savedUser.getEmail(), savedUser.getName(), savedUser.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String token = authService.authenticate(email, password);
        User user = userService.findByEmail(email);

        return ResponseEntity.ok(new AuthResponse(
                token, user.getEmail(), user.getName(), user.getRole()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(UserDTO.from(user));
    }

    @PutMapping("/birthdate")
    public ResponseEntity<MessageResponse> updateBirthDate(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody Map<String, String> body) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Geen geldige token gevonden"));
        }

        if (!body.containsKey("birthDate")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("birthDate is verplicht"));
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        LocalDate birthDate = LocalDate.parse(body.get("birthDate"));
        userService.updateBirthDate(email, birthDate);

        return ResponseEntity.ok(
                new MessageResponse("Geboortedatum opgeslagen")
        );
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody UserDTO updatedUser
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        User user = userService.findByEmail(email);
        user.setName(updatedUser.name());
        user.setEmail(updatedUser.email());
        User saved = userService.save(user);
        return ResponseEntity.ok(UserDTO.from(saved));
    }

    @DeleteMapping("/account")
    public ResponseEntity<MessageResponse> deleteOwnAccount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);
        userService.deleteUser(email);

        return ResponseEntity.ok(
                new MessageResponse("Account verwijderd")
        );
    }


    @PutMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody PasswordChangeRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Geen geldige token gevonden"));
        }

        if (request.getOldPassword() == null || request.getNewPassword() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Beide wachtwoordvelden zijn verplicht."));
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.validateTokenAndGetEmail(token);

        userService.changePassword(
                email,
                request.getOldPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                new MessageResponse("Wachtwoord succesvol gewijzigd")
        );
    }

}
