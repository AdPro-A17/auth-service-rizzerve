package rizzerve.authservice.service.dashboard;

import rizzerve.authservice.dto.dashboard.DashboardResponse;
import rizzerve.authservice.model.Admin;

public interface DashboardService {
    DashboardResponse getDashboardData(Admin admin);
}