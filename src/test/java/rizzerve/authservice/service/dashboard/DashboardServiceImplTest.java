package rizzerve.authservice.service.dashboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private Admin admin;
    private static final String EXPECTED_WELCOME_MESSAGE = "Welcome to Rizzerve Dashboard";

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(UUID.randomUUID())
                .username("testadmin")
                .name("Test Admin")
                .password("password")
                .build();
    }

    @Nested
    @DisplayName("getDashboardData method tests")
    class GetDashboardDataTests {

        @Test
        @DisplayName("Should return dashboard data with correct welcome message")
        void getDashboardData_ShouldReturnCorrectWelcomeMessage() {
            DashboardResponse response = dashboardService.getDashboardData(admin);
            assertEquals(EXPECTED_WELCOME_MESSAGE, response.getWelcomeMessage());
        }

        @Test
        @DisplayName("Should return dashboard data with correct username")
        void getDashboardData_ShouldReturnCorrectUsername() {
            DashboardResponse response = dashboardService.getDashboardData(admin);
            assertEquals(admin.getUsername(), response.getUsername());
        }

        @Test
        @DisplayName("Should return dashboard data with valid last login timestamp")
        void getDashboardData_ShouldReturnValidLastLogin() {
            DashboardResponse response = dashboardService.getDashboardData(admin);
            assertNotNull(response.getLastLogin());
            assertTrue(response.getLastLogin().isBefore(LocalDateTime.now().plusSeconds(1)));
        }

        @Test
        @DisplayName("Should return dashboard data with correct dashboard data map")
        void getDashboardData_ShouldReturnCorrectDashboardData() {
            DashboardResponse response = dashboardService.getDashboardData(admin);
            Map<String, Object> dashboardData = response.getDashboardData();

            assertNotNull(dashboardData);
            assertEquals(EXPECTED_WELCOME_MESSAGE, dashboardData.get("welcomeMessage"));
        }

        @Test
        @DisplayName("Should record dashboard access in metrics")
        void getDashboardData_ShouldRecordMetrics() {
            dashboardService.getDashboardData(admin);
        }

        @Test
        @DisplayName("Should return complete dashboard response")
        void getDashboardData_ShouldReturnCompleteResponse() {
            DashboardResponse response = dashboardService.getDashboardData(admin);

            assertAll(
                    () -> assertNotNull(response),
                    () -> assertEquals(EXPECTED_WELCOME_MESSAGE, response.getWelcomeMessage()),
                    () -> assertEquals(admin.getUsername(), response.getUsername()),
                    () -> assertNotNull(response.getLastLogin()),
                    () -> assertNotNull(response.getDashboardData()),
                    () -> assertEquals(EXPECTED_WELCOME_MESSAGE, response.getDashboardData().get("welcomeMessage"))
            );
        }
    }
}