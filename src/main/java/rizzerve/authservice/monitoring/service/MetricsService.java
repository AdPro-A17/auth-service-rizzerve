package rizzerve.authservice.monitoring.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public void recordSuccessfulLogin(String username) {
        Counter.builder("auth.login.success")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public void recordFailedLogin(String username, String reason) {
        Counter.builder("auth.login.failed")
                .tag("username", username)
                .tag("reason", reason)
                .register(meterRegistry)
                .increment();
    }

    public void recordRegistration(String username) {
        Counter.builder("auth.registration.success")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public void recordDashboardAccess(String username) {
        Counter.builder("dashboard.access")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public void recordTokenGeneration() {
        Counter.builder("auth.token.generated")
                .register(meterRegistry)
                .increment();
    }

    public void recordTokenValidation(boolean valid) {
        Counter.builder("auth.token.validation")
                .tag("result", valid ? "valid" : "invalid")
                .register(meterRegistry)
                .increment();
    }

    public void recordAccountDeletion(String username) {
        Counter.builder("auth.account.deleted")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public void recordProfileUpdate(String username) {
        Counter.builder("auth.profile.updated")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }
}