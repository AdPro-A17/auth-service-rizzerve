package rizzerve.authservice.service.admin;

import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;

public interface AdminAuthenticationService {
    AdminAuthResponse authenticateAdmin(AdminLoginRequest request);
}