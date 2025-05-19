package rizzerve.authservice.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    @Override
    public DashboardResponse getDashboardData(Admin admin) {
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