package rizzerve.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rizzerve.authservice.service.metrics.MetricsService;

@RestController
@RequestMapping("/api/test/metrics")
@RequiredArgsConstructor
public class MetricsTestController {

    private final MetricsService metricsService;

    @PostMapping("/simulate-registration")
    public String simulateRegistration() {
        metricsService.incrementAdminRegistrationCounter();
        return "Registration metric incremented";
    }

    @PostMapping("/simulate-login-success")
    public String simulateLoginSuccess() {
        metricsService.incrementAdminLoginSuccessCounter();
        return "Login success metric incremented";
    }

    @PostMapping("/simulate-login-failure")
    public String simulateLoginFailure(@RequestParam(defaultValue = "test") String reason) {
        metricsService.incrementAdminLoginFailureCounter(reason);
        return "Login failure metric incremented with reason: " + reason;
    }

    @PostMapping("/simulate-profile-update")
    public String simulateProfileUpdate() {
        metricsService.incrementAdminProfileUpdateCounter();
        return "Profile update metric incremented";
    }

    @PostMapping("/simulate-account-delete")
    public String simulateAccountDelete() {
        metricsService.incrementAdminAccountDeleteCounter();
        return "Account delete metric incremented";
    }

    @PostMapping("/simulate-token-invalidation")
    public String simulateTokenInvalidation() {
        metricsService.incrementAdminTokenInvalidationCounter();
        return "Token invalidation metric incremented";
    }
}