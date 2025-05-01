package rizzerve.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .username("testuser")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void getAdminDashboard_Success() {
        when(userRepository.count()).thenReturn(5L);

        DashboardResponse response = userService.getAdminDashboard(authentication);

        assertNotNull(response);
        assertEquals("Welcome to Admin Dashboard", response.getWelcomeMessage());
        assertEquals("testuser", response.getUsername());
        assertEquals("CUSTOMER", response.getRole());
        assertNotNull(response.getLastLogin());

        Map<String, Object> dashboardData = response.getDashboardData();
        assertEquals(5L, dashboardData.get("totalUsers"));
        assertEquals(true, dashboardData.get("adminPrivileges"));

        verify(userRepository).count();
    }

    @Test
    void getCustomerDashboard_Success() {
        DashboardResponse response = userService.getCustomerDashboard(authentication);

        assertNotNull(response);
        assertEquals("Welcome to Customer Dashboard", response.getWelcomeMessage());
        assertEquals("testuser", response.getUsername());
        assertEquals("CUSTOMER", response.getRole());
        assertNotNull(response.getLastLogin());

        Map<String, Object> dashboardData = response.getDashboardData();
        assertEquals(true, dashboardData.get("customerFeatures"));
    }

    @Test
    void updateProfile_Success() {
        ProfileRequest request = new ProfileRequest();
        request.setName("Updated Name");

        when(userRepository.save(any(User.class))).thenReturn(user);

        ProfileResponse response = userService.updateProfile(request, authentication);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Updated Name", response.getName());
        assertEquals(Role.CUSTOMER, response.getRole());

        verify(userRepository).save(any(User.class));
    }
}