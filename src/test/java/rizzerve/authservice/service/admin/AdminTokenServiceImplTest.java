package rizzerve.authservice.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdminTokenServiceImplTest {

    private AdminTokenServiceImpl adminTokenService;

    @BeforeEach
    void setUp() {
        adminTokenService = new AdminTokenServiceImpl();
    }

    @Test
    void invalidateToken_ShouldAddTokenToBlacklist_WhenTokenIsValid() {
        String token = "valid.jwt.token";

        adminTokenService.invalidateToken(token);

        assertThat(adminTokenService.isTokenBlacklisted(token)).isTrue();
    }

    @Test
    void invalidateToken_ShouldNotAddToBlacklist_WhenTokenIsEmpty() {
        String emptyToken = "";

        adminTokenService.invalidateToken(emptyToken);

        assertThat(adminTokenService.isTokenBlacklisted(emptyToken)).isFalse();
    }

    @Test
    void isTokenBlacklisted_ShouldReturnFalse_WhenTokenNotInBlacklist() {
        String token = "not.blacklisted.token";

        boolean result = adminTokenService.isTokenBlacklisted(token);

        assertThat(result).isFalse();
    }

    @Test
    void isTokenBlacklisted_ShouldReturnTrue_WhenTokenIsBlacklisted() {
        String token = "blacklisted.token";
        adminTokenService.invalidateToken(token);

        boolean result = adminTokenService.isTokenBlacklisted(token);

        assertThat(result).isTrue();
    }

    @Test
    void multipleTokens_ShouldHandleIndependently() {
        String token1 = "token.one";
        String token2 = "token.two";

        adminTokenService.invalidateToken(token1);

        assertThat(adminTokenService.isTokenBlacklisted(token1)).isTrue();
        assertThat(adminTokenService.isTokenBlacklisted(token2)).isFalse();
    }
}