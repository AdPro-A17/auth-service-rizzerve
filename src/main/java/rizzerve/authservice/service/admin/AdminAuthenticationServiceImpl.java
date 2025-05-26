package rizzerve.authservice.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.exception.AdminNotFoundException;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;
import rizzerve.authservice.security.token.TokenClaimsExtractor;
import rizzerve.authservice.security.token.TokenService;
import io.micrometer.core.annotation.Timed;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthenticationServiceImpl implements AdminAuthenticationService {

    private final AdminRepository adminRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Timed(value = "auth.login.duration", description = "Time taken for admin login")
    public AdminAuthResponse authenticateAdmin(AdminLoginRequest request) {
        log.debug("Attempting authentication for admin: {}", request.getUsername());

        try {
            performAuthentication(request);
            Admin admin = findAdminByUsername(request.getUsername());
            String token = generateTokenForAdmin(admin);

            log.info("Admin authentication successful for: {}", request.getUsername());
            return buildAuthResponse(admin, token);

        } catch (AuthenticationException e) {
            log.warn("Authentication failed for admin: {}", request.getUsername());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during authentication for admin: {}", request.getUsername(), e);
            throw new RuntimeException("Authentication failed", e);
        }
    }

    private void performAuthentication(AdminLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
    }

    private Admin findAdminByUsername(String username) {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found: " + username));
    }

    private String generateTokenForAdmin(Admin admin) {
        return tokenService.generateToken(admin, TokenClaimsExtractor.extractAdminClaims(admin));
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
