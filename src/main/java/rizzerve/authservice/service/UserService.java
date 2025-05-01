package rizzerve.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public DashboardResponse getAdminDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalUsers", userRepository.count());
        dashboardData.put("adminPrivileges", true);

        return DashboardResponse.builder()
                .welcomeMessage("Welcome to Admin Dashboard")
                .username(user.getUsername())
                .role(user.getRole().name())
                .lastLogin(LocalDateTime.now())
                .dashboardData(dashboardData)
                .build();
    }

    public DashboardResponse getCustomerDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("customerFeatures", true);

        return DashboardResponse.builder()
                .welcomeMessage("Welcome to Customer Dashboard")
                .username(user.getUsername())
                .role(user.getRole().name())
                .lastLogin(LocalDateTime.now())
                .dashboardData(dashboardData)
                .build();
    }

    public ProfileResponse updateProfile(ProfileRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setName(request.getName());
        userRepository.save(user);

        return ProfileResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}