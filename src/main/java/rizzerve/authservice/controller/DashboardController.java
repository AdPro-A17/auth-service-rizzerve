package rizzerve.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rizzerve.authservice.dto.DashboardResponse;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {
    private final UserService userService;

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DashboardResponse> adminDashboard(Authentication authentication) {
        return ResponseEntity.ok(userService.getAdminDashboard(authentication));
    }

    @GetMapping("/customer/dashboard")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<DashboardResponse> customerDashboard(Authentication authentication) {
        return ResponseEntity.ok(userService.getCustomerDashboard(authentication));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestBody ProfileRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(userService.updateProfile(request, authentication));
    }
}