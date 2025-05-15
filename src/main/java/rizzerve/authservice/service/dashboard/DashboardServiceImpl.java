package rizzerve.authservice.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.model.CustomerSession;
import rizzerve.authservice.repository.CustomerSessionRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CustomerSessionRepository customerSessionRepository;

    @Override
    public DashboardResponse getDashboardData(Admin admin) {
        List<CustomerSession> activeSessions = customerSessionRepository.findAll().stream()
                .filter(CustomerSession::isActive)
                .collect(Collectors.toList());

        int totalActiveSessions = activeSessions.size();

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("activeSessions", totalActiveSessions);
        dashboardData.put("activeTables", activeSessions.stream()
                .map(CustomerSession::getTableNumber)
                .collect(Collectors.toList()));

        return DashboardResponse.builder()
                .welcomeMessage("Welcome to Rizzerve Dashboard")
                .username(admin.getUsername())
                .lastLogin(LocalDateTime.now())
                .dashboardData(dashboardData)
                .build();
    }
}