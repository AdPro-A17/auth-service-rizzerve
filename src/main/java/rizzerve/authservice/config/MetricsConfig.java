package rizzerve.authservice.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterFilter commonTagsMeterFilter() {
        return MeterFilter.commonTags(
                Tags.of(
                        "application", "auth-service",
                        "environment", "production"
                )
        );
    }

    @Bean
    public MeterFilter customMeterFilter() {
        return MeterFilter.deny(id ->
                id.getName().startsWith("jvm.gc.pause") ||
                        id.getName().startsWith("process") ||
                        id.getName().startsWith("system")
        );
    }
}