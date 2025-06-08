package rizzerve.authservice.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminRegisterRequest;
import rizzerve.authservice.exception.AdminAlreadyExistsException;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;
import rizzerve.authservice.security.token.TokenClaimsExtractor;
import rizzerve.authservice.security.token.TokenService;
import rizzerve.authservice.service.metrics.MetricsService;
import io.micrometer.core.instrument.Timer;

@Service
@RequiredArgsConstructor
public class AdminRegistrationServiceImpl implements AdminRegistrationService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MetricsService metricsService;

    @Override
    public AdminAuthResponse registerAdmin(AdminRegisterRequest request) {
        Timer.Sample registrationTimer = metricsService.startAdminRegistrationTimer();

        try {
            if(adminRepository.existsByUsername(request.getUsername())) {
                throw new AdminAlreadyExistsException("Username already exists");
            }

            Admin admin = buildAdminFromRequest(request);
            Admin savedAdmin = adminRepository.save(admin);

            String token = tokenService.generateToken(savedAdmin,
                    TokenClaimsExtractor.extractAdminClaims(savedAdmin));

            metricsService.incrementAdminRegistrationCounter();
            metricsService.recordAdminRegistrationDuration(registrationTimer);

            return buildAuthResponse(savedAdmin, token);

        } catch (Exception e) {
            metricsService.recordAdminRegistrationDuration(registrationTimer);
            throw e;
        }
    }

    private Admin buildAdminFromRequest(AdminRegisterRequest request) {
        return Admin.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    private AdminAuthResponse buildAuthResponse(Admin admin, String token) {
        return AdminAuthResponse.builder()
                .token(token)
                .adminId(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .build();
    }
}