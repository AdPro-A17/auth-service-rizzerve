package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;
import rizzerve.authservice.model.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthResponseTest {

    @Test
    void builderAndGettersTest() {
        AuthResponse response = AuthResponse.builder()
                .token("test.jwt.token")
                .username("testuser")
                .name("Test User")
                .role(Role.CUSTOMER)
                .build();

        assertNotNull(response);
        assertEquals("test.jwt.token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getName());
        assertEquals(Role.CUSTOMER, response.getRole());
    }

    @Test
    void settersTest() {
        AuthResponse response = new AuthResponse();
        response.setToken("test.jwt.token");
        response.setUsername("testuser");
        response.setName("Test User");
        response.setRole(Role.CUSTOMER);

        assertEquals("test.jwt.token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getName());
        assertEquals(Role.CUSTOMER, response.getRole());
    }
}