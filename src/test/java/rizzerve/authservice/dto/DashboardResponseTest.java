package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

class DashboardResponseTest {

    @Test
    void builderShouldCreateObjectWithCorrectValues() {
        String welcomeMessage = "Welcome to Dashboard";
        String username = "test@example.com";
        String role = "CUSTOMER";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("key", "value");

        DashboardResponse response = DashboardResponse.builder()
                .welcomeMessage(welcomeMessage)
                .username(username)
                .role(role)
                .lastLogin(lastLogin)
                .dashboardData(dashboardData)
                .build();

        assertEquals(welcomeMessage, response.getWelcomeMessage());
        assertEquals(username, response.getUsername());
        assertEquals(role, response.getRole());
        assertEquals(lastLogin, response.getLastLogin());
        assertEquals(dashboardData, response.getDashboardData());
    }

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        DashboardResponse response = new DashboardResponse();
        String welcomeMessage = "Welcome to Dashboard";
        String username = "test@example.com";
        String role = "CUSTOMER";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("key", "value");

        response.setWelcomeMessage(welcomeMessage);
        response.setUsername(username);
        response.setRole(role);
        response.setLastLogin(lastLogin);
        response.setDashboardData(dashboardData);

        assertEquals(welcomeMessage, response.getWelcomeMessage());
        assertEquals(username, response.getUsername());
        assertEquals(role, response.getRole());
        assertEquals(lastLogin, response.getLastLogin());
        assertEquals(dashboardData, response.getDashboardData());
    }
}