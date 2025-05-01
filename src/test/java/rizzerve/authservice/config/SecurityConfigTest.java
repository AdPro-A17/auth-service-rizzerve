package rizzerve.authservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;
import rizzerve.authservice.repository.UserRepository;
import rizzerve.authservice.security.JwtAuthFilter;
import rizzerve.authservice.security.JwtService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class SecurityConfigTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void jwtAuthFilterBeanCreation() {
        JwtAuthFilter filter = securityConfig.jwtAuthFilter();
        assertNotNull(filter);
    }

    @Test
    void corsConfigurationSourceBeanCreation() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);
    }

    @Test
    void userDetailsServiceBeanCreation() {
        when(userRepository.findByUsername(anyString())).thenThrow(new RuntimeException("User not found"));

        UserDetailsService service = securityConfig.userDetailsService();
        assertNotNull(service);
    }

    @Test
    void authenticationProviderBeanCreation() {
        when(userRepository.findByUsername(anyString())).thenThrow(new RuntimeException("User not found"));

        AuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider);
    }

    @Test
    void passwordEncoderBeanCreation() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }
}