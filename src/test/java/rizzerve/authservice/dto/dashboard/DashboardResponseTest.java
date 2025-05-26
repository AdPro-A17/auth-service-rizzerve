package rizzerve.authservice.dto.dashboard;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class DashboardResponseTest {

    @Test
    void testNoArgsConstructor() {
        DashboardResponse response = new DashboardResponse();
        assertNotNull(response);
        assertNull(response.getWelcomeMessage());
        assertNull(response.getUsername());
        assertNull(response.getLastLogin());
        assertNull(response.getDashboardData());
    }

    @Test
    void testAllArgsConstructor() {
        String welcomeMessage = "Welcome";
        String username = "testuser";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("key", "value");

        DashboardResponse response = new DashboardResponse(welcomeMessage, username, lastLogin, dashboardData);

        assertEquals(welcomeMessage, response.getWelcomeMessage());
        assertEquals(username, response.getUsername());
        assertEquals(lastLogin, response.getLastLogin());
        assertEquals(dashboardData, response.getDashboardData());
    }

    @Test
    void testBuilder() {
        String welcomeMessage = "Welcome";
        String username = "testuser";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("key", "value");

        DashboardResponse response = DashboardResponse.builder()
                .welcomeMessage(welcomeMessage)
                .username(username)
                .lastLogin(lastLogin)
                .dashboardData(dashboardData)
                .build();

        assertEquals(welcomeMessage, response.getWelcomeMessage());
        assertEquals(username, response.getUsername());
        assertEquals(lastLogin, response.getLastLogin());
        assertEquals(dashboardData, response.getDashboardData());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponse response = new DashboardResponse();
        String welcomeMessage = "Welcome";
        String username = "testuser";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("key", "value");

        response.setWelcomeMessage(welcomeMessage);
        response.setUsername(username);
        response.setLastLogin(lastLogin);
        response.setDashboardData(dashboardData);

        assertEquals(welcomeMessage, response.getWelcomeMessage());
        assertEquals(username, response.getUsername());
        assertEquals(lastLogin, response.getLastLogin());
        assertEquals(dashboardData, response.getDashboardData());
    }

    @Test
    void testEqualsAndHashCode() {
        String welcomeMessage = "Welcome";
        String username = "testuser";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("key", "value");

        DashboardResponse response1 = new DashboardResponse(welcomeMessage, username, lastLogin, dashboardData);
        DashboardResponse response2 = new DashboardResponse(welcomeMessage, username, lastLogin, dashboardData);
        DashboardResponse response3 = new DashboardResponse("different", username, lastLogin, dashboardData);
        DashboardResponse response4 = new DashboardResponse(welcomeMessage, "different", lastLogin, dashboardData);
        DashboardResponse response5 = new DashboardResponse(welcomeMessage, username, LocalDateTime.now().plusDays(1), dashboardData);

        Map<String, Object> differentData = new HashMap<>();
        differentData.put("key2", "value2");
        DashboardResponse response6 = new DashboardResponse(welcomeMessage, username, lastLogin, differentData);

        DashboardResponse response7 = new DashboardResponse(null, username, lastLogin, dashboardData);
        DashboardResponse response8 = new DashboardResponse(welcomeMessage, null, lastLogin, dashboardData);
        DashboardResponse response9 = new DashboardResponse(welcomeMessage, username, null, dashboardData);
        DashboardResponse response10 = new DashboardResponse(welcomeMessage, username, lastLogin, null);
        DashboardResponse response11 = new DashboardResponse(null, null, null, null);
        DashboardResponse response12 = new DashboardResponse(null, null, null, null);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        assertNotEquals(response1, response3);
        assertNotEquals(response1, response4);
        assertNotEquals(response1, response5);
        assertNotEquals(response1, response6);
        assertNotEquals(response1, response7);
        assertNotEquals(response1, response8);
        assertNotEquals(response1, response9);
        assertNotEquals(response1, response10);

        assertEquals(response11, response12);
        assertEquals(response11.hashCode(), response12.hashCode());

        assertNotEquals(response7, response8);
        assertNotEquals(response8, response9);
        assertNotEquals(response9, response10);
        assertNotEquals(response7, response11);
    }

    @Test
    void testToString() {
        String welcomeMessage = "Welcome";
        String username = "testuser";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("key", "value");

        DashboardResponse response = new DashboardResponse(welcomeMessage, username, lastLogin, dashboardData);
        String toString = response.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("DashboardResponse"));
        assertTrue(toString.contains(welcomeMessage));
        assertTrue(toString.contains(username));
    }

    @Test
    void testEqualsWithNull() {
        DashboardResponse response = new DashboardResponse();
        DashboardResponse response_new = new DashboardResponse();
        assertNotEquals(null, response);
        assertEquals(response_new, response);

        DashboardResponse response2 = new DashboardResponse("welcome", null, null, null);
        DashboardResponse response3 = new DashboardResponse(null, "user", null, null);
        DashboardResponse response4 = new DashboardResponse(null, null, LocalDateTime.now(), null);

        assertNotEquals(response, response2);
        assertNotEquals(response2, response3);
        assertNotEquals(response3, response4);
    }

    @Test
    void testEqualsWithDifferentClass() {
        DashboardResponse response = new DashboardResponse();
        assertNotEquals(new Object(), response);
    }

    @Test
    void testEqualsWithSameReference() {
        DashboardResponse response = new DashboardResponse();
        DashboardResponse response_new = new DashboardResponse();
        assertEquals(response_new, response);
    }

    @Test
    void canEqual() {
        DashboardResponse response1 = new DashboardResponse();
        DashboardResponse response2 = new DashboardResponse();
        assertTrue(response1.canEqual(response2));
        assertFalse(response1.canEqual("string"));
        assertFalse(response1.canEqual(null));
        assertTrue(response1.canEqual(response1));
    }

    @Test
    void testWithNullValues() {
        DashboardResponse response = new DashboardResponse(null, null, null, null);
        assertNull(response.getWelcomeMessage());
        assertNull(response.getUsername());
        assertNull(response.getLastLogin());
        assertNull(response.getDashboardData());
    }
}