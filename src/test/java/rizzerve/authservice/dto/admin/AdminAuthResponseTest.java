package rizzerve.authservice.dto.admin;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AdminAuthResponseTest {

    @Test
    void testNoArgsConstructor() {
        AdminAuthResponse response = new AdminAuthResponse();
        assertNotNull(response);
        assertNull(response.getToken());
        assertNull(response.getAdminId());
        assertNull(response.getUsername());
        assertNull(response.getName());
    }

    @Test
    void testAllArgsConstructor() {
        String token = "test-token";
        UUID adminId = UUID.randomUUID();
        String username = "testuser";
        String name = "Test User";

        AdminAuthResponse response = new AdminAuthResponse(token, adminId, username, name);

        assertEquals(token, response.getToken());
        assertEquals(adminId, response.getAdminId());
        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
    }

    @Test
    void testBuilder() {
        String token = "test-token";
        UUID adminId = UUID.randomUUID();
        String username = "testuser";
        String name = "Test User";

        AdminAuthResponse response = AdminAuthResponse.builder()
                .token(token)
                .adminId(adminId)
                .username(username)
                .name(name)
                .build();

        assertEquals(token, response.getToken());
        assertEquals(adminId, response.getAdminId());
        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
    }

    @Test
    void testSettersAndGetters() {
        AdminAuthResponse response = new AdminAuthResponse();
        String token = "test-token";
        UUID adminId = UUID.randomUUID();
        String username = "testuser";
        String name = "Test User";

        response.setToken(token);
        response.setAdminId(adminId);
        response.setUsername(username);
        response.setName(name);

        assertEquals(token, response.getToken());
        assertEquals(adminId, response.getAdminId());
        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        String token = "test-token";
        UUID adminId = UUID.randomUUID();
        String username = "testuser";
        String name = "Test User";

        AdminAuthResponse response1 = new AdminAuthResponse(token, adminId, username, name);
        AdminAuthResponse response2 = new AdminAuthResponse(token, adminId, username, name);
        AdminAuthResponse response3 = new AdminAuthResponse("different", adminId, username, name);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testToString() {
        String token = "test-token";
        UUID adminId = UUID.randomUUID();
        String username = "testuser";
        String name = "Test User";

        AdminAuthResponse response = new AdminAuthResponse(token, adminId, username, name);
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("AdminAuthResponse"));
        assertTrue(toString.contains(token));
        assertTrue(toString.contains(adminId.toString()));
        assertTrue(toString.contains(username));
        assertTrue(toString.contains(name));
    }

    @Test
    void testEqualsWithNull() {
        AdminAuthResponse response = new AdminAuthResponse();
        assertNotEquals(response, null);
        assertNotEquals(null, response);
    }

    @Test
    void testEqualsWithSameReference() {
        AdminAuthResponse response = new AdminAuthResponse();
        AdminAuthResponse response_new = new AdminAuthResponse();
        assertEquals(response, response_new);
    }

    @Test
    void canEqual() {
        AdminAuthResponse response1 = new AdminAuthResponse();
        AdminAuthResponse response2 = new AdminAuthResponse();
        assertTrue(response1.canEqual(response2));
        assertFalse(response1.canEqual("string"));
    }
}