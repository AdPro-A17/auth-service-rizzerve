package rizzerve.authservice.security.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigsTest {

    @Autowired
    private SecurityAuthProviderConfig authProviderConfig;

    @Autowired
    private SecurityCorsConfig corsConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoderShouldBeConfiguredCorrectly() {
        assertNotNull(passwordEncoder);

        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void corsConfigurationSourceShouldBeConfigured() {
        CorsConfigurationSource corsSource = corsConfig.corsConfigurationSource();
        assertNotNull(corsSource);
    }

    @Test
    void authProviderConfigShouldProvideUserDetailsService() {
        assertNotNull(authProviderConfig.userDetailsService());
    }
}