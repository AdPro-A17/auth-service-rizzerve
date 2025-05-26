package rizzerve.authservice.service.admin;

import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminRegisterRequest;

public interface AdminRegistrationService {
    AdminAuthResponse registerAdmin(AdminRegisterRequest request);
}