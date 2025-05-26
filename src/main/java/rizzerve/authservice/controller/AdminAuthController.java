package rizzerve.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.dto.admin.AdminRegisterRequest;
import rizzerve.authservice.dto.admin.AdminUpdateNameRequest;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.service.admin.AdminAuthenticationService;
import rizzerve.authservice.service.admin.AdminProfileService;
import rizzerve.authservice.service.admin.AdminRegistrationService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminRegistrationService registrationService;
    private final AdminAuthenticationService authenticationService;
    private final AdminProfileService profileService;
    private final AdminManagementService managementService;
    private final AdminTokenService tokenService;

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

    @PutMapping("/profile/name")
    public ResponseEntity<AdminProfileResponse> updateName(
            @AuthenticationPrincipal Admin admin,
            @Valid @RequestBody AdminUpdateNameRequest request) {
        AdminProfileResponse response = managementService.updateAdminName(admin, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenService.invalidateToken(token);
        }
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @DeleteMapping("/account")
    public ResponseEntity<Map<String, String>> deleteAccount(
            @AuthenticationPrincipal Admin admin,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenService.invalidateToken(token);
        }

        managementService.deleteAdminAccount(admin);
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }
}