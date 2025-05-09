package rizzerve.authservice.service.dashboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.model.CustomerSession;
import rizzerve.authservice.repository.CustomerSessionRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTest {

    @Mock
    private CustomerSessionRepository customerSessionRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private Admin admin;
    private List<CustomerSession> sessions;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .name("Test Admin")
                .password("password")
                .build();

        CustomerSession activeSession1 = CustomerSession.builder()
                .id(UUID.randomUUID())
                .tableNumber(1)
                .sessionToken("token1")
                .startTime(LocalDateTime.now().minusHours(1))
                .active(true)
                .build();

        CustomerSession activeSession2 = CustomerSession.builder()
                .id(UUID.randomUUID())
                .tableNumber(2)
                .sessionToken("token2")
                .startTime(LocalDateTime.now().minusMinutes(30))
                .active(true)
                .build();

        CustomerSession inactiveSession = CustomerSession.builder()
                .id(UUID.randomUUID())
                .tableNumber(3)
                .sessionToken("token3")
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().minusHours(1))
                .active(false)
                .build();

        sessions = Arrays.asList(activeSession1, activeSession2, inactiveSession);
    }

    @Test
    void getDashboardData_ShouldReturnCorrectData() {
        when(customerSessionRepository.findAll()).thenReturn(sessions);

        DashboardResponse response = dashboardService.getDashboardData(admin);

        assertNotNull(response);
        assertEquals("Welcome to Rizzerve Dashboard", response.getWelcomeMessage());
        assertEquals(admin.getUsername(), response.getUsername());
        assertNotNull(response.getLastLogin());

        Map<String, Object> dashboardData = response.getDashboardData();
        assertNotNull(dashboardData);
        assertEquals(2, dashboardData.get("activeSessions"));

        List<Integer> activeTables = (List<Integer>) dashboardData.get("activeTables");
        assertNotNull(activeTables);
        assertEquals(2, activeTables.size());
        assertTrue(activeTables.contains(1));
        assertTrue(activeTables.contains(2));
        assertFalse(activeTables.contains(3));
    }

    @Test
    void getDashboardData_ShouldReturnEmptyData_WhenNoActiveSessions() {
        CustomerSession inactiveSession = CustomerSession.builder()
                .id(UUID.randomUUID())
                .tableNumber(3)
                .sessionToken("token3")
                .startTime(LocalDateTime.now().minusHours(2))
                .endTime(LocalDateTime.now().minusHours(1))
                .active(false)
                .build();

        when(customerSessionRepository.findAll()).thenReturn(List.of(inactiveSession));

        DashboardResponse response = dashboardService.getDashboardData(admin);

        assertNotNull(response);

        Map<String, Object> dashboardData = response.getDashboardData();
        assertNotNull(dashboardData);
        assertEquals(0, dashboardData.get("activeSessions"));

        List<Integer> activeTables = (List<Integer>) dashboardData.get("activeTables");
        assertNotNull(activeTables);
        assertEquals(0, activeTables.size());
    }
}
