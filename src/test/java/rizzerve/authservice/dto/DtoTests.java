package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.dto.admin.AdminRegisterRequest;
import rizzerve.authservice.dto.customer.CustomerSessionRequest;
import rizzerve.authservice.dto.customer.CustomerSessionResponse;
import rizzerve.authservice.dto.customer.EndSessionRequest;
import rizzerve.authservice.dto.dashboard.DashboardResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoTests {

    @Test
    void adminAuthResponseTest() {
        String token = "test-token";
        UUID adminId = UUID.randomUUID();
        String username = "testadmin";
        String name = "Test Admin";

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
    void adminLoginRequestTest() {
        String username = "testadmin";
        String password = "password123";

        AdminLoginRequest request = AdminLoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    @Test
    void adminProfileResponseTest() {
        String username = "testadmin";
        String name = "Test Admin";

        AdminProfileResponse response = AdminProfileResponse.builder()
                .username(username)
                .name(name)
                .build();

        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
    }

    @Test
    void adminRegisterRequestTest() {
        String name = "Test Admin";
        String username = "testadmin";
        String password = "password123";

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
    void customerSessionRequestTest() {
        Integer tableNumber = 1;

        CustomerSessionRequest request = CustomerSessionRequest.builder()
                .tableNumber(tableNumber)
                .build();

        assertEquals(tableNumber, request.getTableNumber());
    }

    @Test
    void customerSessionResponseTest() {
        String status = "success";
        UUID sessionId = UUID.randomUUID();
        String sessionToken = "test-session-token";
        Integer tableNumber = 1;
        LocalDateTime startTime = LocalDateTime.now();

        CustomerSessionResponse response = CustomerSessionResponse.builder()
                .status(status)
                .sessionId(sessionId)
                .sessionToken(sessionToken)
                .tableNumber(tableNumber)
                .startTime(startTime)
                .build();

        assertEquals(status, response.getStatus());
        assertEquals(sessionId, response.getSessionId());
        assertEquals(sessionToken, response.getSessionToken());
        assertEquals(tableNumber, response.getTableNumber());
        assertEquals(startTime, response.getStartTime());
    }

    @Test
    void endSessionRequestTest() {
        String sessionToken = "test-session-token";

        EndSessionRequest request = EndSessionRequest.builder()
                .sessionToken(sessionToken)
                .build();

        assertEquals(sessionToken, request.getSessionToken());
    }

    @Test
    void dashboardResponseTest() {
        String welcomeMessage = "Welcome to Dashboard";
        String username = "testadmin";
        LocalDateTime lastLogin = LocalDateTime.now();
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("activeSessions", 5);

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
        assertEquals(5, response.getDashboardData().get("activeSessions"));
    }
}
