package rizzerve.authservice.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rizzerve.authservice.model.Admin;
import io.jsonwebtoken.io.Decoders;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService tokenService;

    private String secretKey;
    private long jwtExpiration;
    private Admin admin;
    private Key signingKey;

    @BeforeEach
    void setUp() {
        secretKey = "5170c563e2abfd66aef4d6e842640a489f7b54d75e036c984d67b7c78113c0c8";
        jwtExpiration = 86400000;

        ReflectionTestUtils.setField(tokenService, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", jwtExpiration);

        admin = Admin.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .name("Admin User")
                .password("password")
                .build();

        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", admin.getId().toString());
        claims.put("name", admin.getName());

        String token = tokenService.generateToken(admin, claims);

        assertNotNull(token);

        Claims parsedClaims = Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(admin.getUsername(), parsedClaims.getSubject());
        assertEquals(admin.getId().toString(), parsedClaims.get("adminId", String.class));
        assertEquals(admin.getName(), parsedClaims.get("name", String.class));
    }

    @Test
    void generateSessionToken_ShouldCreateValidToken() {
        Integer tableNumber = 5;

        String token = tokenService.generateSessionToken(tableNumber);

        assertNotNull(token);

        Claims parsedClaims = Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(tableNumber, parsedClaims.get("tableNumber", Integer.class));
    }

    @Test
    void validateToken_ShouldReturnTrue_ForValidToken() {
        String token = Jwts.builder()
                .setSubject(admin.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        boolean isValid = tokenService.validateToken(token, admin);

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenUsernameMismatch() {
        Admin otherAdmin = Admin.builder()
                .username("other-admin")
                .build();

        String token = Jwts.builder()
                .setSubject(admin.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        boolean isValid = tokenService.validateToken(token, otherAdmin);

        assertFalse(isValid);
    }

    @Test
    void validateSessionToken_ShouldReturnTrue_ForValidToken() {
        String token = Jwts.builder()
                .claim("tableNumber", 5)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        boolean isValid = tokenService.validateSessionToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateSessionToken_ShouldReturnFalse_WhenTokenExpired() {
        String token = Jwts.builder()
                .claim("tableNumber", 5)
                .setIssuedAt(new Date(System.currentTimeMillis() - 2 * jwtExpiration))
                .setExpiration(new Date(System.currentTimeMillis() - jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        boolean isValid = tokenService.validateSessionToken(token);

        assertFalse(isValid);
    }

    @Test
    void extractUsername_ShouldReturnUsername() {
        String token = Jwts.builder()
                .setSubject(admin.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        String username = tokenService.extractUsername(token);

        assertEquals(admin.getUsername(), username);
    }

    @Test
    void extractAdminId_ShouldReturnAdminId() {
        String token = Jwts.builder()
                .setSubject(admin.getUsername())
                .claim("adminId", admin.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        UUID adminId = tokenService.extractAdminId(token);

        assertEquals(admin.getId(), adminId);
    }

    @Test
    void extractTableNumber_ShouldReturnTableNumber() {
        Integer tableNumber = 5;
        String token = Jwts.builder()
                .claim("tableNumber", tableNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        Integer extractedTableNumber = tokenService.extractTableNumber(token);

        assertEquals(tableNumber, extractedTableNumber);
    }
}
