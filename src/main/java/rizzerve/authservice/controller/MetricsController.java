package rizzerve.authservice.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Meter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MeterRegistry meterRegistry;

    @GetMapping("/custom")
    public Map<String, Object> getCustomMetrics() {
        return meterRegistry.getMeters().stream()
                .filter(meter -> meter.getId().getName().startsWith("admin."))
                .collect(Collectors.toMap(
                        meter -> meter.getId().getName(),
                        meter -> meter.measure()
                ));
    }
}