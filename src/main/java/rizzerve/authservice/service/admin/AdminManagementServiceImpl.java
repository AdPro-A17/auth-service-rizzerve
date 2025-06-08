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
import rizzerve.authservice.service.metrics.MetricsService;
import io.micrometer.core.annotation.Timed;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminManagementServiceImpl implements AdminManagementService {

    private final AdminRepository adminRepository;
    private final MetricsService metricsService;

    @Override
    @Transactional
    public AdminProfileResponse updateAdminName(Admin admin, AdminUpdateNameRequest request) {
        log.debug("Updating name for admin: {}", admin.getUsername());

        try {
            Admin existingAdmin = adminRepository.findById(admin.getId())
                    .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

            existingAdmin.setName(request.getName());
            Admin updatedAdmin = adminRepository.save(existingAdmin);

            metricsService.incrementAdminProfileUpdateCounter();

            log.info("Admin name updated successfully for: {}", admin.getUsername());

            return AdminProfileResponse.builder()
                    .username(updatedAdmin.getUsername())
                    .name(updatedAdmin.getName())
                    .build();
        } catch (Exception e) {
            log.error("Failed to update admin profile for: {}", admin.getUsername(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    @Timed(value = "admin.delete.account.duration", description = "Time taken to delete admin account")
    public void deleteAdminAccount(Admin admin) {
        log.debug("Deleting account for admin: {}", admin.getUsername());

        try {
            Admin existingAdmin = adminRepository.findById(admin.getId())
                    .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

            adminRepository.delete(existingAdmin);

            metricsService.incrementAdminAccountDeleteCounter();

            log.info("Admin account deleted successfully for: {}", admin.getUsername());
        } catch (Exception e) {
            log.error("Failed to delete admin account for: {}", admin.getUsername(), e);
            throw e;
        }
    }
}