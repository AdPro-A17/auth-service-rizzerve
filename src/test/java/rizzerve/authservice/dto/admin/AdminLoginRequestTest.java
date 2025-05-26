package rizzerve.authservice.dto.admin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminLoginRequestTest {

    @Test
    void testNoArgsConstructor() {
        AdminLoginRequest request = new AdminLoginRequest();
        assertNotNull(request);
        assertNull(request.getUsername());
        assertNull(request.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        String username = "testuser";
        String password = "testpass";

        AdminLoginRequest request = new AdminLoginRequest(username, password);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testBuilder() {
        String username = "testuser";
        String password = "testpass";

        AdminLoginRequest request = AdminLoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        AdminLoginRequest request = new AdminLoginRequest();
        String username = "testuser";
        String password = "testpass";

        request.setUsername(username);
        request.setPassword(password);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        String username = "testuser";
        String password = "testpass";

        AdminLoginRequest request1 = new AdminLoginRequest(username, password);
        AdminLoginRequest request2 = new AdminLoginRequest(username, password);
        AdminLoginRequest request3 = new AdminLoginRequest("different", password);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        String username = "testuser";
        String password = "testpass";

        AdminLoginRequest request = new AdminLoginRequest(username, password);
        String toString = request.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("AdminLoginRequest"));
        assertTrue(toString.contains(username));
        assertTrue(toString.contains(password));
    }

    @Test
    void testEqualsWithNull() {
        AdminLoginRequest request = new AdminLoginRequest();
        assertNotEquals(null, request);
    }

    @Test
    void testEqualsWithSameReference() {
        AdminLoginRequest request = new AdminLoginRequest();
        AdminLoginRequest request2 = new AdminLoginRequest();
        assertEquals(request2, request);
    }

    @Test
    void canEqual() {
        AdminLoginRequest request1 = new AdminLoginRequest();
        AdminLoginRequest request2 = new AdminLoginRequest();
        assertTrue(request1.canEqual(request2));
        assertFalse(request1.canEqual("string"));
    }
}