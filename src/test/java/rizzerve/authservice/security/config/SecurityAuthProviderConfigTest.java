package rizzerve.authservice.security.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import rizzerve.authservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityAuthProviderConfigTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityAuthProviderConfig securityAuthProviderConfig;

    @Test
    void passwordEncoderShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityAuthProviderConfig.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    void userDetailsServiceShouldReturnImplementation() {
        UserDetailsService userDetailsService = securityAuthProviderConfig.userDetailsService();

        assertNotNull(userDetailsService);
    }

    @Test
    void authenticationProviderShouldReturnConfiguredProvider() {
        SecurityAuthProviderConfig spyConfig = spy(securityAuthProviderConfig);
        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        PasswordEncoder mockEncoder = mock(PasswordEncoder.class);

        doReturn(mockUserDetailsService).when(spyConfig).userDetailsService();
        doReturn(mockEncoder).when(spyConfig).passwordEncoder();

        AuthenticationProvider provider = spyConfig.authenticationProvider();
        assertNotNull(provider);
        verify(spyConfig).userDetailsService();
        verify(spyConfig).passwordEncoder();
    }

    @Test
    void getSessionCreationPolicyShouldReturnStateless() {
        assertEquals(org.springframework.security.config.http.SessionCreationPolicy.STATELESS,
                securityAuthProviderConfig.getSessionCreationPolicy());
    }
}