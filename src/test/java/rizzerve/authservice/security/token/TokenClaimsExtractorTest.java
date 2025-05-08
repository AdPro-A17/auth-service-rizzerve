package rizzerve.authservice.security.token;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenClaimsExtractorTest {

    @Test
    void extractUserClaimsShouldReturnExpectedClaims() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .username("test@example.com")
                .role(Role.CUSTOMER)
                .build();

        Map<String, Object> claims = TokenClaimsExtractor.extractUserClaims(user);

        assertNotNull(claims);
        assertEquals(3, claims.size());
        assertEquals(userId.toString(), claims.get("userId"));
        assertEquals(Role.CUSTOMER.name(), claims.get("role"));
        assertEquals("Test User", claims.get("name"));
    }

    @Test
    void extractUserClaimsWithNullIdShouldNotIncludeUserId() {
        User user = User.builder()
                .id(null)
                .name("Test User")
                .username("test@example.com")
                .role(Role.CUSTOMER)
                .build();

        Map<String, Object> claims = TokenClaimsExtractor.extractUserClaims(user);

        assertNotNull(claims);
        assertEquals(2, claims.size());
        assertFalse(claims.containsKey("userId"));
        assertEquals(Role.CUSTOMER.name(), claims.get("role"));
        assertEquals("Test User", claims.get("name"));
    }
}