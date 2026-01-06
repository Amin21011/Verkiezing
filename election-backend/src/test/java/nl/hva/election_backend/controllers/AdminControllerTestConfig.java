package nl.hva.election_backend.controllers;
import nl.hva.election_backend.repository.UserRepository;
import nl.hva.election_backend.security.JwtUtil;
import nl.hva.election_backend.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AdminControllerTestConfig {
    @Bean
    UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }
}
