package rizzerve.authservice.security.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.security.filter.JwtAuthFilter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityEndpointConfigTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @InjectMocks
    private SecurityEndpointConfig securityEndpointConfig;

    @Test
    void getPublicEndpointsShouldReturnExpectedList() {
        List<String> publicEndpoints = securityEndpointConfig.getPublicEndpoints();

        assertNotNull(publicEndpoints);
        assertEquals(2, publicEndpoints.size());
        assertTrue(publicEndpoints.contains("/api/auth/**"));
        assertTrue(publicEndpoints.contains("/h2-console/**"));
    }

    @Test
    void getAdminEndpointsShouldReturnExpectedList() {
        List<String> adminEndpoints = securityEndpointConfig.getAdminEndpoints();

        assertNotNull(adminEndpoints);
        assertEquals(1, adminEndpoints.size());
        assertTrue(adminEndpoints.contains("/api/admin/**"));
    }

    @Test
    void getCustomerEndpointsShouldReturnExpectedList() {
        List<String> customerEndpoints = securityEndpointConfig.getCustomerEndpoints();

        assertNotNull(customerEndpoints);
        assertEquals(1, customerEndpoints.size());
        assertTrue(customerEndpoints.contains("/api/customer/**"));
    }
}