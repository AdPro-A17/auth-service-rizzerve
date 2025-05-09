package rizzerve.authservice.security.token;

import rizzerve.authservice.model.Admin;
import java.util.HashMap;
import java.util.Map;

public class TokenClaimsExtractor {
    public static Map<String, Object> extractAdminClaims(Admin admin) {
        Map<String, Object> claims = new HashMap<>();

        if (admin.getId() != null) {
            claims.put("adminId", admin.getId().toString());
        }
        claims.put("name", admin.getName());

        return claims;
    }
}