package rizzerve.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private String welcomeMessage;
    private String username;
    private String role;
    private LocalDateTime lastLogin;
    private Map<String, Object> dashboardData;
}