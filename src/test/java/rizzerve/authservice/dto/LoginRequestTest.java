package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        LoginRequest request = new LoginRequest();
        String username = "test@example.com";
        String password = "password";

        request.setUsername(username);
        request.setPassword(password);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }
}