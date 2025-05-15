package rizzerve.authservice.security.token;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import java.util.UUID;

public interface TokenService {
    String generateToken(UserDetails userDetails, Map<String, Object> claims);
    String generateSessionToken(Integer tableNumber);
    boolean validateToken(String token, UserDetails userDetails);
    boolean validateSessionToken(String token);
    String extractUsername(String token);
    UUID extractAdminId(String token);
    Integer extractTableNumber(String token);
}