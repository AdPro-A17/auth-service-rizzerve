package rizzerve.authservice.dto.admin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminRegisterRequestTest {

    @Test
    void testNoArgsConstructor() {
        AdminRegisterRequest request = new AdminRegisterRequest();
        assertNotNull(request);
        assertNull(request.getName());
        assertNull(request.getUsername());
        assertNull(request.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        String name = "Test User";
        String username = "testuser";
        String password = "testpass";

        AdminRegisterRequest request = new AdminRegisterRequest(name, username, password);

        assertEquals(name, request.getName());
        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testBuilder() {
        String name = "Test User";
        String username = "testuser";
        String password = "testpass";

        AdminRegisterRequest request = AdminRegisterRequest.builder()
                .name(name)
                .username(username)
                .password(password)
                .build();

        assertEquals(name, request.getName());
        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        AdminRegisterRequest request = new AdminRegisterRequest();
        String name = "Test User";
        String username = "testuser";
        String password = "testpass";

        request.setName(name);
        request.setUsername(username);
        request.setPassword(password);

        assertEquals(name, request.getName());
        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        String name = "Test User";
        String username = "testuser";
        String password = "testpass";

        AdminRegisterRequest request1 = new AdminRegisterRequest(name, username, password);
        AdminRegisterRequest request2 = new AdminRegisterRequest(name, username, password);
        AdminRegisterRequest request3 = new AdminRegisterRequest("different", username, password);
        AdminRegisterRequest request4 = new AdminRegisterRequest(name, "different", password);
        AdminRegisterRequest request5 = new AdminRegisterRequest(name, username, "different");
        AdminRegisterRequest request6 = new AdminRegisterRequest(null, username, password);
        AdminRegisterRequest request7 = new AdminRegisterRequest(name, null, password);
        AdminRegisterRequest request8 = new AdminRegisterRequest(name, username, null);
        AdminRegisterRequest request9 = new AdminRegisterRequest(null, null, null);
        AdminRegisterRequest request10 = new AdminRegisterRequest(null, null, null);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request5);
        assertNotEquals(request1, request6);
        assertNotEquals(request1, request7);
        assertNotEquals(request1, request8);
        assertNotEquals(request1, request9);

        assertEquals(request9, request10);
        assertEquals(request9.hashCode(), request10.hashCode());

        assertNotEquals(request6, request7);
        assertNotEquals(request7, request8);
        assertNotEquals(request6, request9);
    }

    @Test
    void testToString() {
        String name = "Test User";
        String username = "testuser";
        String password = "testpass";

        AdminRegisterRequest request = new AdminRegisterRequest(name, username, password);
        String toString = request.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("AdminRegisterRequest"));
        assertTrue(toString.contains(name));
        assertTrue(toString.contains(username));
        assertTrue(toString.contains(password));
    }

    @Test
    void testEqualsWithNull() {
        AdminRegisterRequest request = new AdminRegisterRequest();
        AdminRegisterRequest request_test = new AdminRegisterRequest();
        assertNotEquals(null, request);
        assertEquals(request_test, request);

        AdminRegisterRequest request2 = new AdminRegisterRequest("name", null, null);
        AdminRegisterRequest request3 = new AdminRegisterRequest(null, "user", null);
        AdminRegisterRequest request4 = new AdminRegisterRequest(null, null, "pass");

        assertNotEquals(request, request2);
        assertNotEquals(request2, request3);
        assertNotEquals(request3, request4);
    }

    @Test
    void testEqualsWithDifferentClass() {
        AdminRegisterRequest request = new AdminRegisterRequest();
        assertNotEquals(new Object(), request);
    }

    @Test
    void testEqualsWithSameReference() {
        AdminRegisterRequest request = new AdminRegisterRequest();
        AdminRegisterRequest request2 = new AdminRegisterRequest();
        assertEquals(request2, request);
    }

    @Test
    void canEqual() {
        AdminRegisterRequest request1 = new AdminRegisterRequest();
        AdminRegisterRequest request2 = new AdminRegisterRequest();
        assertTrue(request1.canEqual(request2));
        assertFalse(request1.canEqual("string"));
        assertFalse(request1.canEqual(null));
        assertTrue(request1.canEqual(request1));
    }
}