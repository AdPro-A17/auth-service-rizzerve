package rizzerve.authservice.service.admin;

public interface AdminTokenService {
    void invalidateToken(String token);
    boolean isTokenBlacklisted(String token);
}