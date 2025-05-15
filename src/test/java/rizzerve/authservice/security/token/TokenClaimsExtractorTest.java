package rizzerve.authservice.security.token;

import org.junit.jupiter.api.Test;
import rizzerve.authservice.model.Admin;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TokenClaimsExtractorTest {

    @Test
    void extractAdminClaims_withValidAdmin_shouldReturnCorrectClaims() {
        UUID adminId = UUID.randomUUID();
        String name = "Test Admin";

        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setName(name);

        Map<String, Object> claims = TokenClaimsExtractor.extractAdminClaims(admin);

        assertNotNull(claims);
        assertEquals(3, claims.size());
        assertEquals(adminId.toString(), claims.get("adminId"));
        assertEquals(name, claims.get("name"));
    }

    @Test
    void extractAdminClaims_withNullAdminId_shouldNotIncludeIdInClaims() {
        String name = "Test Admin";

        Admin admin = new Admin();
        admin.setId(null);
        admin.setName(name);

        Map<String, Object> claims = TokenClaimsExtractor.extractAdminClaims(admin);

        assertNotNull(claims);
        assertEquals(2, claims.size());
        assertFalse(claims.containsKey("adminId"));
        assertEquals(name, claims.get("name"));
    }

    @Test
    void extractAdminClaims_withNullName_shouldIncludeNullName() {
        UUID adminId = UUID.randomUUID();

        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setName(null);

        Map<String, Object> claims = TokenClaimsExtractor.extractAdminClaims(admin);

        assertNotNull(claims);
        assertEquals(3, claims.size());
        assertEquals(adminId.toString(), claims.get("adminId"));
        assertNull(claims.get("name"));
    }
}
