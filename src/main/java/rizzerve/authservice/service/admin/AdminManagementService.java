package rizzerve.authservice.service.admin;

import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.dto.admin.AdminUpdateNameRequest;
import rizzerve.authservice.model.Admin;

public interface AdminManagementService {
    AdminProfileResponse updateAdminName(Admin admin, AdminUpdateNameRequest request);
    void deleteAdminAccount(Admin admin);
}