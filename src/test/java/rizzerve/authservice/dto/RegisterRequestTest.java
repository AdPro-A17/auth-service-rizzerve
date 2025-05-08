package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;
import rizzerve.authservice.model.Role;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        RegisterRequest request = new RegisterRequest();
        String username = "test@example.com";
        String password = "password";
        String name = "Test User";
        Role role = Role.ADMIN;

        request.setUsername(username);
        request.setPassword(password);
        request.setName(name);
        request.setRole(role);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
        assertEquals(name, request.getName());
        assertEquals(role, request.getRole());
    }
}