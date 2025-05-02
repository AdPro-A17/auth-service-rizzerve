package rizzerve.authservice.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDashboardServiceImpl implements UserDashboardService {
    private final UserRepository userRepository;

    @Override
    public DashboardResponse getAdminDashboard(User user) {
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalUsers", userRepository.count());
        dashboardData.put("adminPrivileges", true);

        return buildDashboardResponse(
                "Welcome to Admin Dashboard",
                user,
                dashboardData
        );
    }

    @Override
    public DashboardResponse getCustomerDashboard(User user) {
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("customerFeatures", true);

        return buildDashboardResponse(
                "Welcome to Customer Dashboard",
                user,
                dashboardData
        );
    }

    private DashboardResponse buildDashboardResponse(
            String welcomeMessage,
            User user,
            Map<String, Object> dashboardData) {

        return DashboardResponse.builder()
                .welcomeMessage(welcomeMessage)
                .username(user.getUsername())
                .role(user.getRole().name())
                .lastLogin(LocalDateTime.now())
                .dashboardData(dashboardData)
                .build();
    }
}
