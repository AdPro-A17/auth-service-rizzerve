package rizzerve.authservice.security.token;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import java.util.UUID;

public interface TokenService {
    String generateToken(UserDetails userDetails, Map<String, Object> claims);
    boolean validateToken(String token, UserDetails userDetails);
    String extractUsername(String token);
    UUID extractAdminId(String token);
    String generateSessionToken(Integer tableNumber);
    boolean validateSessionToken(String token);
    Integer extractTableNumber(String token);
}