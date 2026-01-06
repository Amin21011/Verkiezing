package nl.hva.election_backend.controllers;
import nl.hva.election_backend.api.AdminController;
import nl.hva.election_backend.model.User;
import nl.hva.election_backend.repository.UserRepository;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(AdminControllerTestConfig.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String VALID_TOKEN = "Bearer valid-token";

    private User adminUser() {
        User admin = new User();
        admin.setId(1L);
        admin.setEmail("admin@test.com");
        admin.setRole("ADMIN");
        return admin;
    }

    private User normalUser(Long id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setName("Test User");
        user.setRole("USER");
        user.setQuizBestMatch("VVD");
        return user;
    }

    @Test
    void getAllUsers_missingAuthorizationHeader_returns400() throws Exception {
        mockMvc.perform(get("/api/auth/admin"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_forbiddenForNonAdmin() throws Exception {
        Mockito.when(jwtUtil.validateTokenAndGetEmail("valid-token"))
                .thenReturn("user@test.com");

        User nonAdmin = normalUser(2L, "user@test.com");

        Mockito.when(userService.findByEmail("user@test.com"))
                .thenReturn(nonAdmin);

        mockMvc.perform(get("/api/auth/admin")
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(containsString("admin")));
    }

    @Test
    void getAllNormalUsers_success() throws Exception {
        Mockito.when(jwtUtil.validateTokenAndGetEmail("valid-token"))
                .thenReturn("admin@test.com");

        Mockito.when(userService.findByEmail("admin@test.com"))
                .thenReturn(adminUser());

        Mockito.when(userRepository.findAll())
                .thenReturn(List.of(
                        normalUser(2L, "user1@test.com"),
                        normalUser(3L, "user2@test.com"),
                        adminUser() // moet eruit gefilterd worden
                ));

        mockMvc.perform(get("/api/auth/admin")
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$[0].quizBestMatch").value("VVD"));
    }

    @Test
    void deleteUser_success() throws Exception {
        Mockito.when(jwtUtil.validateTokenAndGetEmail("valid-token"))
                .thenReturn("admin@test.com");

        Mockito.when(userService.findByEmail("admin@test.com"))
                .thenReturn(adminUser());

        User target = normalUser(2L, "user@test.com");

        Mockito.when(userRepository.findById(2L))
                .thenReturn(Optional.of(target));

        mockMvc.perform(delete("/api/auth/admin/2")
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());

        Mockito.verify(userRepository).delete(target);
    }

    @Test
    void deleteUser_notFound() throws Exception {
        Mockito.when(jwtUtil.validateTokenAndGetEmail("valid-token"))
                .thenReturn("admin@test.com");

        Mockito.when(userService.findByEmail("admin@test.com"))
                .thenReturn(adminUser());

        Mockito.when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/auth/admin/99")
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateRole_success() throws Exception {
        Mockito.when(jwtUtil.validateTokenAndGetEmail("valid-token"))
                .thenReturn("admin@test.com");

        Mockito.when(userService.findByEmail("admin@test.com"))
                .thenReturn(adminUser());

        User user = normalUser(2L, "user@test.com");

        Mockito.when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        mockMvc.perform(put("/api/auth/admin/2/role")
                        .param("role", "ADMIN")
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void updateRole_invalidRole() throws Exception {
        Mockito.when(jwtUtil.validateTokenAndGetEmail("valid-token"))
                .thenReturn("admin@test.com");

        Mockito.when(userService.findByEmail("admin@test.com"))
                .thenReturn(adminUser());

        Mockito.when(userRepository.findById(2L))
                .thenReturn(Optional.of(normalUser(2L, "user@test.com")));

        mockMvc.perform(put("/api/auth/admin/2/role")
                        .param("role", "SUPERADMIN")
                        .header("Authorization", VALID_TOKEN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
