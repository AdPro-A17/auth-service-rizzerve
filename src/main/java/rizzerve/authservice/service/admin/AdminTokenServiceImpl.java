package rizzerve.authservice.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rizzerve.authservice.service.metrics.MetricsService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTokenServiceImpl implements AdminTokenService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final MetricsService metricsService;

    @Override
    public void invalidateToken(String token) {
        if (token != null && !token.isEmpty()) {
            blacklistedTokens.add(token);

            metricsService.incrementAdminTokenInvalidationCounter();

            log.debug("Token added to blacklist");
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}