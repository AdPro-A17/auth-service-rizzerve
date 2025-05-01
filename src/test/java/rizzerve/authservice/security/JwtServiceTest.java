package rizzerve.authservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:jwt.properties")
@ActiveProfiles("test")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .username("testuser")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    void extractUsername() {
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void generateToken() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void isTokenValid() {
        String token = jwtService.generateToken(user);
        boolean valid = jwtService.isTokenValid(token, user);
        assertTrue(valid);
    }

    @Test
    void isTokenInvalidForDifferentUser() {
        String token = jwtService.generateToken(user);

        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("differentuser");

        boolean valid = jwtService.isTokenValid(token, differentUser);
        assertFalse(valid);
    }
}