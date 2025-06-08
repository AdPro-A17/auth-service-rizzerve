package rizzerve.authservice.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;

    private static final String ADMIN_REGISTRATION_COUNTER = "admin.registration.total";
    private static final String ADMIN_LOGIN_SUCCESS_COUNTER = "admin.login.success.total";
    private static final String ADMIN_LOGIN_FAILURE_COUNTER = "admin.login.failure.total";
    private static final String ADMIN_PROFILE_UPDATE_COUNTER = "admin.profile.update.total";
    private static final String ADMIN_ACCOUNT_DELETE_COUNTER = "admin.account.delete.total";
    private static final String ADMIN_TOKEN_INVALIDATION_COUNTER = "admin.token.invalidation.total";
    private static final String ADMIN_LOGIN_DURATION = "admin.login.duration";
    private static final String ADMIN_REGISTRATION_DURATION = "admin.registration.duration";

    public void incrementAdminRegistrationCounter() {
        Counter.builder(ADMIN_REGISTRATION_COUNTER)
                .description("Total number of admin registrations")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .register(meterRegistry)
                .increment();
        log.debug("Admin registration counter incremented");
    }

    public void incrementAdminLoginSuccessCounter() {
        Counter.builder(ADMIN_LOGIN_SUCCESS_COUNTER)
                .description("Total number of successful admin logins")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .tag("status", "success")
                .register(meterRegistry)
                .increment();
        log.debug("Admin login success counter incremented");
    }

    public void incrementAdminLoginFailureCounter(String reason) {
        Counter.builder(ADMIN_LOGIN_FAILURE_COUNTER)
                .description("Total number of failed admin logins")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .tag("status", "failure")
                .tag("reason", reason != null ? reason : "unknown")
                .register(meterRegistry)
                .increment();
        log.debug("Admin login failure counter incremented with reason: {}", reason);
    }

    public void incrementAdminProfileUpdateCounter() {
        Counter.builder(ADMIN_PROFILE_UPDATE_COUNTER)
                .description("Total number of admin profile updates")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .tag("action", "profile_update")
                .register(meterRegistry)
                .increment();
        log.debug("Admin profile update counter incremented");
    }

    public void incrementAdminAccountDeleteCounter() {
        Counter.builder(ADMIN_ACCOUNT_DELETE_COUNTER)
                .description("Total number of admin account deletions")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .tag("action", "account_delete")
                .register(meterRegistry)
                .increment();
        log.debug("Admin account delete counter incremented");
    }

    public void incrementAdminTokenInvalidationCounter() {
        Counter.builder(ADMIN_TOKEN_INVALIDATION_COUNTER)
                .description("Total number of admin token invalidations")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .tag("action", "token_invalidation")
                .register(meterRegistry)
                .increment();
        log.debug("Admin token invalidation counter incremented");
    }

    public Timer.Sample startAdminLoginTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordAdminLoginDuration(Timer.Sample sample) {
        sample.stop(Timer.builder(ADMIN_LOGIN_DURATION)
                .description("Time taken for admin login process")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .register(meterRegistry));
        log.debug("Admin login duration recorded");
    }

    public Timer.Sample startAdminRegistrationTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordAdminRegistrationDuration(Timer.Sample sample) {
        sample.stop(Timer.builder(ADMIN_REGISTRATION_DURATION)
                .description("Time taken for admin registration process")
                .tag("service", "auth-service")
                .tag("type", "admin")
                .register(meterRegistry));
        log.debug("Admin registration duration recorded");
    }

    public void incrementCustomCounter(String counterName, String description, String... tags) {
        Counter.Builder builder = Counter.builder(counterName)
                .description(description)
                .tag("service", "auth-service");

        for (int i = 0; i < tags.length - 1; i += 2) {
            builder.tag(tags[i], tags[i + 1]);
        }

        builder.register(meterRegistry).increment();
        log.debug("Custom counter {} incremented", counterName);
    }
}
