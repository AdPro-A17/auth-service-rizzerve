package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;
import rizzerve.authservice.model.Role;

import static org.junit.jupiter.api.Assertions.*;

class ProfileResponseTest {

    @Test
    void builderShouldCreateObjectWithCorrectValues() {
        String username = "test@example.com";
        String name = "Test User";
        Role role = Role.CUSTOMER;

        ProfileResponse response = ProfileResponse.builder()
                .username(username)
                .name(name)
                .role(role)
                .build();

        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
        assertEquals(role, response.getRole());
    }

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        ProfileResponse response = new ProfileResponse();
        String username = "test@example.com";
        String name = "Test User";
        Role role = Role.CUSTOMER;

        response.setUsername(username);
        response.setName(name);
        response.setRole(role);

        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
        assertEquals(role, response.getRole());
    }
}