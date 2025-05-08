package rizzerve.authservice.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDashboardServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDashboardServiceImpl userDashboardService;

    @Test
    void getAdminDashboardShouldReturnCorrectData() {
        User adminUser = User.builder()
                .id(UUID.randomUUID())
                .username("admin@example.com")
                .name("Admin User")
                .role(Role.ADMIN)
                .build();

        when(userRepository.count()).thenReturn(10L);

        DashboardResponse response = userDashboardService.getAdminDashboard(adminUser);

        assertNotNull(response);
        assertEquals("Welcome to Admin Dashboard", response.getWelcomeMessage());
        assertEquals("admin@example.com", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        assertNotNull(response.getLastLogin());

        Map<String, Object> dashboardData = response.getDashboardData();
        assertNotNull(dashboardData);
        assertEquals(10L, dashboardData.get("totalUsers"));
        assertEquals(true, dashboardData.get("adminPrivileges"));

        verify(userRepository).count();
    }

    @Test
    void getCustomerDashboardShouldReturnCorrectData() {
        User customerUser = User.builder()
                .id(UUID.randomUUID())
                .username("customer@example.com")
                .name("Customer User")
                .role(Role.CUSTOMER)
                .build();

        DashboardResponse response = userDashboardService.getCustomerDashboard(customerUser);

        assertNotNull(response);
        assertEquals("Welcome to Customer Dashboard", response.getWelcomeMessage());
        assertEquals("customer@example.com", response.getUsername());
        assertEquals("CUSTOMER", response.getRole());
        assertNotNull(response.getLastLogin());

        Map<String, Object> dashboardData = response.getDashboardData();
        assertNotNull(dashboardData);
        assertEquals(true, dashboardData.get("customerFeatures"));

        verifyNoInteractions(userRepository);
    }
}
