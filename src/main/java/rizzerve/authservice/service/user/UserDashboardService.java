package rizzerve.authservice.service.user;

import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.model.User;

public interface UserDashboardService {
    DashboardResponse getAdminDashboard(User user);
    DashboardResponse getCustomerDashboard(User user);
}