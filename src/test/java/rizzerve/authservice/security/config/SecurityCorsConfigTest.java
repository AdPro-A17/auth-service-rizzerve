package rizzerve.authservice.security.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityCorsConfigTest {

    @Test
    void corsConfigurationSourceShouldReturnValidConfiguration() {
        SecurityCorsConfig corsConfig = new SecurityCorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();

        assertNotNull(source);
    }

    @Test
    void getAllowedOriginsShouldReturnExpectedList() {
        SecurityCorsConfig corsConfig = new SecurityCorsConfig();
        List<String> allowedOrigins = corsConfig.getAllowedOrigins();

        assertNotNull(allowedOrigins);
        assertEquals(1, allowedOrigins.size());
        assertTrue(allowedOrigins.contains("http://localhost:3000"));
    }

    @Test
    void getAllowedMethodsShouldReturnExpectedList() {
        SecurityCorsConfig corsConfig = new SecurityCorsConfig();
        List<String> allowedMethods = corsConfig.getAllowedMethods();

        assertNotNull(allowedMethods);
        assertEquals(5, allowedMethods.size());
        assertTrue(allowedMethods.containsAll(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")));
    }

    @Test
    void getAllowedHeadersShouldReturnExpectedList() {
        SecurityCorsConfig corsConfig = new SecurityCorsConfig();
        List<String> allowedHeaders = corsConfig.getAllowedHeaders();

        assertNotNull(allowedHeaders);
        assertEquals(2, allowedHeaders.size());
        assertTrue(allowedHeaders.containsAll(List.of("Authorization", "Content-Type")));
    }
}