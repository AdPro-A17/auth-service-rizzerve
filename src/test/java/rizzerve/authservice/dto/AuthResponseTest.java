package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;
import rizzerve.authservice.model.Role;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void builderShouldCreateObjectWithCorrectValues() {
        UUID userId = UUID.randomUUID();
        String token = "jwt.token.here";
        String username = "test@example.com";
        String name = "Test User";
        Role role = Role.CUSTOMER;

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .userId(userId)
                .username(username)
                .name(name)
                .role(role)
                .build();

        assertEquals(token, response.getToken());
        assertEquals(userId, response.getUserId());
        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
        assertEquals(role, response.getRole());
    }

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        AuthResponse response = new AuthResponse();
        UUID userId = UUID.randomUUID();
        String token = "jwt.token.here";
        String username = "test@example.com";
        String name = "Test User";
        Role role = Role.CUSTOMER;

        response.setToken(token);
        response.setUserId(userId);
        response.setUsername(username);
        response.setName(name);
        response.setRole(role);

        assertEquals(token, response.getToken());
        assertEquals(userId, response.getUserId());
        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
        assertEquals(role, response.getRole());
    }
}