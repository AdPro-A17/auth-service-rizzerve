package rizzerve.authservice.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.dto.admin.AdminUpdateNameRequest;
import rizzerve.authservice.exception.AdminNotFoundException;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;
import io.micrometer.core.annotation.Timed;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminManagementServiceImpl implements AdminManagementService {

    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public AdminProfileResponse updateAdminName(Admin admin, AdminUpdateNameRequest request) {
        log.debug("Updating name for admin: {}", admin.getUsername());

        Admin existingAdmin = adminRepository.findById(admin.getId())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        existingAdmin.setName(request.getName());
        Admin updatedAdmin = adminRepository.save(existingAdmin);
        log.info("Admin name updated successfully for: {}", admin.getUsername());

        return AdminProfileResponse.builder()
                .username(updatedAdmin.getUsername())
                .name(updatedAdmin.getName())
                .build();
    }

    @Override
    @Transactional
    @Timed(value = "admin.delete.account.duration", description = "Time taken to delete admin account")
    public void deleteAdminAccount(Admin admin) {
        log.debug("Deleting account for admin: {}", admin.getUsername());

        Admin existingAdmin = adminRepository.findById(admin.getId())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        adminRepository.delete(existingAdmin);

        log.info("Admin account deleted successfully for: {}", admin.getUsername());
    }
}