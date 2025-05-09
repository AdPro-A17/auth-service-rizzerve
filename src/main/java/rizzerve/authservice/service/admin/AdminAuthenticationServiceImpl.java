package rizzerve.authservice.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.exception.AdminNotFoundException;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;
import rizzerve.authservice.security.token.TokenClaimsExtractor;
import rizzerve.authservice.security.token.TokenService;

@Service
@RequiredArgsConstructor
public class AdminAuthenticationServiceImpl implements AdminAuthenticationService {
    private final AdminRepository adminRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AdminAuthResponse authenticateAdmin(AdminLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        String token = tokenService.generateToken(admin, TokenClaimsExtractor.extractAdminClaims(admin));

        return buildAuthResponse(admin, token);
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