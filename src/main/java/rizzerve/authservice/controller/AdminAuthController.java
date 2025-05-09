package rizzerve.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.dto.admin.AdminRegisterRequest;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.service.admin.AdminAuthenticationService;
import rizzerve.authservice.service.admin.AdminProfileService;
import rizzerve.authservice.service.admin.AdminRegistrationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminRegistrationService registrationService;
    private final AdminAuthenticationService authenticationService;
    private final AdminProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<AdminAuthResponse> register(@Valid @RequestBody AdminRegisterRequest request) {
        AdminAuthResponse response = registrationService.registerAdmin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminAuthResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        AdminAuthResponse response = authenticationService.authenticateAdmin(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<AdminProfileResponse> getProfile(@AuthenticationPrincipal Admin admin) {
        AdminProfileResponse profileResponse = profileService.getAdminProfile(admin);
        return ResponseEntity.ok(profileResponse);
    }
}