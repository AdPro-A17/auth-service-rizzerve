package rizzerve.authservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DashboardController dashboardController;

    private DashboardResponse dashboardResponse;
    private ProfileResponse profileResponse;

    @BeforeEach
    void setUp() {
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalUsers", 5L);
        dashboardData.put("adminPrivileges", true);

        dashboardResponse = DashboardResponse.builder()
                .welcomeMessage("Welcome")
                .username("testuser")
                .role("ADMIN")
                .lastLogin(null)
                .dashboardData(dashboardData)
                .build();

        profileResponse = ProfileResponse.builder()
                .username("testuser")
                .name("Test User")
                .role(null)
                .build();
    }

    @Test
    void adminDashboard_Success() {
        when(userService.getAdminDashboard(any(Authentication.class))).thenReturn(dashboardResponse);

        ResponseEntity<DashboardResponse> response = dashboardController.adminDashboard(authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Welcome", response.getBody().getWelcomeMessage());
        assertEquals("testuser", response.getBody().getUsername());

        verify(userService).getAdminDashboard(authentication);
    }

    @Test
    void customerDashboard_Success() {
        when(userService.getCustomerDashboard(any(Authentication.class))).thenReturn(dashboardResponse);

        ResponseEntity<DashboardResponse> response = dashboardController.customerDashboard(authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Welcome", response.getBody().getWelcomeMessage());

        verify(userService).getCustomerDashboard(authentication);
    }

    @Test
    void updateProfile_Success() {
        ProfileRequest request = new ProfileRequest();
        request.setName("Updated Name");

        when(userService.updateProfile(any(ProfileRequest.class), any(Authentication.class)))
                .thenReturn(profileResponse);

        ResponseEntity<ProfileResponse> response = dashboardController.updateProfile(request, authentication);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testuser", response.getBody().getUsername());

        verify(userService).updateProfile(request, authentication);
    }
}