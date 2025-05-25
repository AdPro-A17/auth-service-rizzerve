package rizzerve.authservice.dto.admin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminProfileResponseTest {

    @Test
    void testNoArgsConstructor() {
        AdminProfileResponse response = new AdminProfileResponse();
        assertNotNull(response);
        assertNull(response.getUsername());
        assertNull(response.getName());
    }

    @Test
    void testAllArgsConstructor() {
        String username = "testuser";
        String name = "Test User";

        AdminProfileResponse response = new AdminProfileResponse(username, name);

        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
    }

    @Test
    void testBuilder() {
        String username = "testuser";
        String name = "Test User";

        AdminProfileResponse response = AdminProfileResponse.builder()
                .username(username)
                .name(name)
                .build();

        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
    }

    @Test
    void testSettersAndGetters() {
        AdminProfileResponse response = new AdminProfileResponse();
        String username = "testuser";
        String name = "Test User";

        response.setUsername(username);
        response.setName(name);

        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        String username = "testuser";
        String name = "Test User";

        AdminProfileResponse response1 = new AdminProfileResponse(username, name);
        AdminProfileResponse response2 = new AdminProfileResponse(username, name);
        AdminProfileResponse response3 = new AdminProfileResponse("different", name);
        AdminProfileResponse response4 = new AdminProfileResponse(username, "different");
        AdminProfileResponse response5 = new AdminProfileResponse(null, name);
        AdminProfileResponse response6 = new AdminProfileResponse(username, null);
        AdminProfileResponse response7 = new AdminProfileResponse(null, null);
        AdminProfileResponse response8 = new AdminProfileResponse(null, null);

        // Test equality
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality
        assertNotEquals(response1, response3);
        assertNotEquals(response1, response4);
        assertNotEquals(response1, response5);
        assertNotEquals(response1, response6);
        assertNotEquals(response1, response7);

        // Test null equality
        assertEquals(response7, response8);
        assertEquals(response7.hashCode(), response8.hashCode());

        // Test partial null inequality
        assertNotEquals(response5, response6);
        assertNotEquals(response5, response7);
    }

    @Test
    void testToString() {
        String username = "testuser";
        String name = "Test User";

        AdminProfileResponse response = new AdminProfileResponse(username, name);
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("AdminProfileResponse"));
        assertTrue(toString.contains(username));
        assertTrue(toString.contains(name));
    }

    @Test
    void testEqualsWithNull() {
        AdminProfileResponse response = new AdminProfileResponse();
        assertNotEquals(response, null);
        assertEquals(response, response);

        // Test with different combinations
        AdminProfileResponse response2 = new AdminProfileResponse("user", null);
        AdminProfileResponse response3 = new AdminProfileResponse(null, "name");
        assertNotEquals(response, response2);
        assertNotEquals(response2, response3);
    }

    @Test
    void testEqualsWithDifferentClass() {
        AdminProfileResponse response = new AdminProfileResponse();
        assertNotEquals(response, "string");
        assertNotEquals(response, 123);
        assertNotEquals(response, new Object());
    }

    @Test
    void testEqualsWithSameReference() {
        AdminProfileResponse response = new AdminProfileResponse();
        assertEquals(response, response);
        assertTrue(response.equals(response));
    }

    @Test
    void canEqual() {
        AdminProfileResponse response1 = new AdminProfileResponse();
        AdminProfileResponse response2 = new AdminProfileResponse();
        assertTrue(response1.canEqual(response2));
        assertFalse(response1.canEqual("string"));
        assertFalse(response1.canEqual(null));
        assertTrue(response1.canEqual(response1));
    }
}