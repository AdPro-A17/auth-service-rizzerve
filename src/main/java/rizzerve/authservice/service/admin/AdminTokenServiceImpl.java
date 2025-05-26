package rizzerve.authservice.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

@Slf4j
@Service
public class AdminTokenServiceImpl implements AdminTokenService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public void invalidateToken(String token) {
        if (token != null && !token.isEmpty()) {
            blacklistedTokens.add(token);
            log.debug("Token added to blacklist");
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}