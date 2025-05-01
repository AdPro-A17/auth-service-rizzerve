package rizzerve.authservice.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rizzerve.authservice.model.Role;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RequestDTOsTest {

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        registerRequest = RegisterRequest.builder()
                .name("Test User")
                .username("testuser")
                .password("password123")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    void loginRequestBuilderAndGetters() {
        assertNotNull(loginRequest);
        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    void loginRequestSetters() {
        LoginRequest request = new LoginRequest();
        request.setUsername("newuser");
        request.setPassword("newpassword");

        assertEquals("newuser", request.getUsername());
        assertEquals("newpassword", request.getPassword());
    }

    @Test
    void registerRequestBuilderAndGetters() {
        assertNotNull(registerRequest);
        assertEquals("Test User", registerRequest.getName());
        assertEquals("testuser", registerRequest.getUsername());
        assertEquals("password123", registerRequest.getPassword());
        assertEquals(Role.CUSTOMER, registerRequest.getRole());
    }

    @Test
    void registerRequestSetters() {
        RegisterRequest request = new RegisterRequest();
        request.setName("New User");
        request.setUsername("newuser");
        request.setPassword("newpassword");
        request.setRole(Role.ADMIN);

        assertEquals("New User", request.getName());
        assertEquals("newuser", request.getUsername());
        assertEquals("newpassword", request.getPassword());
        assertEquals(Role.ADMIN, request.getRole());
    }
}