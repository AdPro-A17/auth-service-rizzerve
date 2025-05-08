package rizzerve.authservice.security.token;

import rizzerve.authservice.model.User;
import java.util.HashMap;
import java.util.Map;

public class TokenClaimsExtractor {
    public static Map<String, Object> extractUserClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        if (user.getId() != null) {
            claims.put("userId", user.getId().toString());
        }
        claims.put("role", user.getRole().name());
        claims.put("name", user.getName());

        return claims;
    }
}