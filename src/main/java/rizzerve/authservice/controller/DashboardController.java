package rizzerve.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.model.User;
import rizzerve.authservice.service.user.UserDashboardService;
import rizzerve.authservice.service.user.UserProfileService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {
    private final UserDashboardService dashboardService;
    private final UserProfileService profileService;

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DashboardResponse> adminDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(dashboardService.getAdminDashboard(user));
    }

    @GetMapping("/customer/dashboard")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<DashboardResponse> customerDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(dashboardService.getCustomerDashboard(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody ProfileRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.updateProfile(request, user));
    }
}