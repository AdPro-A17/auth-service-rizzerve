package rizzerve.authservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;
import rizzerve.authservice.service.user.UserDashboardService;
import rizzerve.authservice.service.user.UserProfileService;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private UserDashboardService dashboardService;

    @Mock
    private UserProfileService profileService;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void adminDashboard_ShouldReturnAdminDashboard() {
        User adminUser = createUser(Role.ADMIN);
        setupSecurityContext(adminUser);

        DashboardResponse expectedResponse = new DashboardResponse();
        when(dashboardService.getAdminDashboard(adminUser)).thenReturn(expectedResponse);

        ResponseEntity<DashboardResponse> response = dashboardController.adminDashboard(
                SecurityContextHolder.getContext().getAuthentication()
        );

        assertEquals(200, response.getStatusCodeValue());
        assertSame(expectedResponse, response.getBody());
        verify(dashboardService).getAdminDashboard(adminUser);
    }

    @Test
    void customerDashboard_ShouldReturnCustomerDashboard() {
        User customerUser = createUser(Role.CUSTOMER);
        setupSecurityContext(customerUser);

        DashboardResponse expectedResponse = new DashboardResponse();
        when(dashboardService.getCustomerDashboard(customerUser)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<DashboardResponse> response = dashboardController.customerDashboard(
                SecurityContextHolder.getContext().getAuthentication()
        );

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertSame(expectedResponse, response.getBody());
        verify(dashboardService).getCustomerDashboard(customerUser);
    }

    @Test
    void updateProfile_ShouldReturnUpdatedProfile() {
        User user = createUser(Role.CUSTOMER);
        setupSecurityContext(user);

        ProfileRequest request = new ProfileRequest("New Name");
        ProfileResponse expectedResponse = new ProfileResponse();
        when(profileService.updateProfile(request, user)).thenReturn(expectedResponse);

        ResponseEntity<ProfileResponse> response = dashboardController.updateProfile(
                request,
                SecurityContextHolder.getContext().getAuthentication()
        );

        assertEquals(200, response.getStatusCodeValue());
        assertSame(expectedResponse, response.getBody());
        verify(profileService).updateProfile(request, user);
    }

    private User createUser(Role role) {
        return User.builder()
                .id(UUID.randomUUID())
                .username(role.name().toLowerCase() + "@example.com")
                .role(role)
                .build();
    }

    private void setupSecurityContext(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}