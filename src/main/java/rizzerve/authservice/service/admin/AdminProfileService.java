package rizzerve.authservice.service.admin;

import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.model.Admin;

public interface AdminProfileService {
    AdminProfileResponse getAdminProfile(Admin admin);
}