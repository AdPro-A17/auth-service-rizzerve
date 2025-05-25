package rizzerve.authservice.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.monitoring.service.MetricsService;
import io.micrometer.core.annotation.Timed;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final MetricsService metricsService;

    @Override
    @Timed(value = "dashboard.load.duration", description = "Time taken to load dashboard")
    public DashboardResponse getDashboardData(Admin admin) {
        metricsService.recordDashboardAccess(admin.getUsername());

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("welcomeMessage", "Welcome to Rizzerve Dashboard");

        return DashboardResponse.builder()
                .welcomeMessage("Welcome to Rizzerve Dashboard")
                .username(admin.getUsername())
                .lastLogin(LocalDateTime.now())
                .dashboardData(dashboardData)
                .build();
    }
}