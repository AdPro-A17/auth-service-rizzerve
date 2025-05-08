package rizzerve.authservice.security.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Mock
    private UserDetails userDetails;

    private final String secretKey = "testsecretkeythatisveryverylongandmustbeatleast256bits";
    private final long jwtExpiration = 86400000; // 24 hours

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenService, "jwtExpiration", jwtExpiration);
    }

    @Test
    void generateTokenShouldCreateValidToken() {
        String username = "test@example.com";
        when(userDetails.getUsername()).thenReturn(username);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", UUID.randomUUID().toString());
        claims.put("role", "CUSTOMER");

        String token = jwtTokenService.generateToken(userDetails, claims);

        assertNotNull(token);

        String extractedUsername = jwtTokenService.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateTokenShouldReturnTrueForValidToken() {
        String username = "test@example.com";
        when(userDetails.getUsername()).thenReturn(username);

        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenService.generateToken(userDetails, claims);

        boolean isValid = jwtTokenService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void validateTokenShouldReturnFalseForInvalidUsername() {
        String username = "test@example.com";
        when(userDetails.getUsername()).thenReturn(username);

        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenService.generateToken(userDetails, claims);

        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("different@example.com");

        boolean isValid = jwtTokenService.validateToken(token, differentUser);

        assertFalse(isValid);
    }

    @Test
    void extractUserIdShouldReturnNullWhenNotPresent() {
        String username = "test@example.com";
        when(userDetails.getUsername()).thenReturn(username);

        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenService.generateToken(userDetails, claims);

        UUID userId = jwtTokenService.extractUserId(token);

        assertNull(userId);
    }

    @Test
    void extractUserIdShouldReturnUuidWhenPresent() {
        String username = "test@example.com";
        when(userDetails.getUsername()).thenReturn(username);

        UUID expectedId = UUID.randomUUID();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", expectedId.toString());

        String token = jwtTokenService.generateToken(userDetails, claims);

        UUID userId = jwtTokenService.extractUserId(token);

        assertNotNull(userId);
        assertEquals(expectedId, userId);
    }
}