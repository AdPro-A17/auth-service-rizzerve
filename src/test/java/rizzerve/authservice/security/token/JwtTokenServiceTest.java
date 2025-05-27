package rizzerve.authservice.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    private JwtTokenService tokenService;
    private UserDetails userDetails;
    private String secretKey;
    private long jwtExpiration;

    @BeforeEach
    void setUp() {
        tokenService = new JwtTokenService();
        secretKey = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi10ZXN0aW5nLXB1cnBvc2Vz";
        jwtExpiration = 86400000L;

        ReflectionTestUtils.setField(tokenService, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", jwtExpiration);

        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");

        String token = tokenService.generateToken(userDetails, claims);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void generateToken_shouldIncludeProvidedClaims() {
        UUID adminId = UUID.randomUUID();
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", adminId.toString());
        claims.put("role", "ADMIN");

        String token = tokenService.generateToken(userDetails, claims);

        Claims extractedClaims = extractClaimsFromToken(token);
        assertEquals(adminId.toString(), extractedClaims.get("adminId"));
        assertEquals("ADMIN", extractedClaims.get("role"));
    }

    @Test
    void generateToken_shouldSetSubjectAsUsername() {
        Map<String, Object> claims = new HashMap<>();

        String token = tokenService.generateToken(userDetails, claims);

        Claims extractedClaims = extractClaimsFromToken(token);
        assertEquals("testuser", extractedClaims.getSubject());
    }

    @Test
    void generateToken_shouldSetIssuedAtAndExpiration() {
        Map<String, Object> claims = new HashMap<>();
        long beforeGeneration = System.currentTimeMillis();

        String token = tokenService.generateToken(userDetails, claims);

        long afterGeneration = System.currentTimeMillis();
        Claims extractedClaims = extractClaimsFromToken(token);

        Date issuedAt = extractedClaims.getIssuedAt();
        Date expiration = extractedClaims.getExpiration();

        assertFalse(issuedAt.getTime() >= beforeGeneration);
        assertTrue(issuedAt.getTime() <= afterGeneration);
        assertEquals(issuedAt.getTime() + jwtExpiration, expiration.getTime());
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        Map<String, Object> claims = new HashMap<>();
        String token = tokenService.generateToken(userDetails, claims);

        String extractedUsername = tokenService.extractUsername(token);

        assertEquals("testuser", extractedUsername);
    }

    @Test
    void extractAdminId_shouldReturnCorrectAdminId() {
        UUID adminId = UUID.randomUUID();
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", adminId.toString());
        String token = tokenService.generateToken(userDetails, claims);

        UUID extractedAdminId = tokenService.extractAdminId(token);

        assertEquals(adminId, extractedAdminId);
    }

    @Test
    void extractAdminId_shouldReturnNullWhenAdminIdNotPresent() {
        Map<String, Object> claims = new HashMap<>();
        String token = tokenService.generateToken(userDetails, claims);

        UUID extractedAdminId = tokenService.extractAdminId(token);

        assertNull(extractedAdminId);
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        Map<String, Object> claims = new HashMap<>();
        String token = tokenService.generateToken(userDetails, claims);

        boolean isValid = tokenService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void validateToken_shouldReturnFalseForDifferentUsername() {
        Map<String, Object> claims = new HashMap<>();
        String token = tokenService.generateToken(userDetails, claims);

        UserDetails differentUser = User.builder()
                .username("differentuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();

        boolean isValid = tokenService.validateToken(token, differentUser);

        assertFalse(isValid);
    }

    @Test
    void generateToken_withEmptyClaims_shouldWork() {
        Map<String, Object> claims = new HashMap<>();

        String token = tokenService.generateToken(userDetails, claims);

        assertNotNull(token);
        assertEquals("testuser", tokenService.extractUsername(token));
    }

    @Test
    void extractAdminId_withInvalidUUID_shouldThrowException() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", "invalid-uuid");
        String token = tokenService.generateToken(userDetails, claims);

        assertThrows(IllegalArgumentException.class, () ->
                tokenService.extractAdminId(token));
    }

    private Claims extractClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}