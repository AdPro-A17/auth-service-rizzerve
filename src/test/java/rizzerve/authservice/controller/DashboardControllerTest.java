package rizzerve.authservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.service.dashboard.DashboardService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    private Admin admin;
    private DashboardResponse dashboardResponse;

    @BeforeEach
    void setup() {
        admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setUsername("testadmin");
        admin.setName("Test Admin");

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("activeSessions", 5);

        dashboardResponse = DashboardResponse.builder()
                .welcomeMessage("Welcome to Rizzerve Dashboard")
                .username("testadmin")
                .lastLogin(LocalDateTime.now())
                .dashboardData(dashboardData)
                .build();
    }

    @Test
    void getDashboardShouldReturnDashboardResponse() {
        when(dashboardService.getDashboardData(any(Admin.class))).thenReturn(dashboardResponse);

        ResponseEntity<DashboardResponse> response = dashboardController.getDashboard(admin);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dashboardResponse, response.getBody());
    }
}