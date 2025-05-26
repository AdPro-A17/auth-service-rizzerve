package rizzerve.authservice.service.dashboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTest {

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .name("Test Admin")
                .password("password")
                .build();
    }

//    @Test
//    void getDashboardData_ShouldReturnCorrectData() {
//
//        DashboardResponse response = dashboardService.getDashboardData(admin);
//
//        assertNotNull(response);
//        assertEquals("Welcome to Rizzerve Dashboard", response.getWelcomeMessage());
//        assertEquals(admin.getUsername(), response.getUsername());
//        assertNotNull(response.getLastLogin());
//
//        Map<String, Object> dashboardData = response.getDashboardData();
//        assertNotNull(dashboardData);
//        assertNull(dashboardData.get("activeSessions"));
//    }
}
