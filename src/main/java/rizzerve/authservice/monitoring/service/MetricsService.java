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

    private static final String AUTH_SUCCESS_COUNTER = "auth.login.success.total";
    private static final String AUTH_FAILURE_COUNTER = "auth.login.failure.total";
    private static final String REGISTRATION_COUNTER = "auth.registration.total";
    private static final String TOKEN_VALIDATION_TIMER = "auth.token.validation.duration";

    private static final String DASHBOARD_ACCESS_COUNTER = "dashboard.access.total";

    public void recordSuccessfulLogin(String username) {
        Counter.builder(AUTH_SUCCESS_COUNTER)
                .description("Number of successful login attempts")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public void recordFailedLogin(String username, String reason) {
        Counter.builder(AUTH_FAILURE_COUNTER)
                .description("Number of failed login attempts")
                .tag("username", username)
                .tag("failure_reason", reason)
                .register(meterRegistry)
                .increment();
    }

    public void recordRegistration(String username) {
        Counter.builder(REGISTRATION_COUNTER)
                .description("Number of admin registrations")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public Timer.Sample startTokenValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordTokenValidation(Timer.Sample sample, boolean isValid) {
        sample.stop(Timer.builder(TOKEN_VALIDATION_TIMER)
                .description("Time taken to validate tokens")
                .tag("valid", String.valueOf(isValid))
                .register(meterRegistry));
    }

    public void recordDashboardAccess(String username) {
        Counter.builder(DASHBOARD_ACCESS_COUNTER)
                .description("Number of dashboard access attempts")
                .tag("username", username)
                .register(meterRegistry)
                .increment();
    }

    public void recordHttpStatus(String endpoint, int statusCode) {
        Counter.builder("http.requests.total")
                .description("Total HTTP requests")
                .tag("endpoint", endpoint)
                .tag("status", String.valueOf(statusCode))
                .register(meterRegistry)
                .increment();
    }
}