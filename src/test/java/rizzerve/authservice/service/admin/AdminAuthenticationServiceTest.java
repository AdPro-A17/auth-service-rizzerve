package rizzerve.authservice.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;
import rizzerve.authservice.security.token.TokenClaimsExtractor;
import rizzerve.authservice.security.token.TokenService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAuthenticationServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AdminAuthenticationServiceImpl adminAuthenticationService;

    private Admin admin;
    private AdminLoginRequest loginRequest;
    private String token;
    private Map<String, Object> claims;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(UUID.randomUUID())
                .username("testadmin")
                .name("Test Admin")
                .password("password")
                .build();

        loginRequest = AdminLoginRequest.builder()
                .username("testadmin")
                .password("password")
                .build();

        token = "test.jwt.token";
        claims = TokenClaimsExtractor.extractAdminClaims(admin);
    }

    @Nested
    @DisplayName("authenticateAdmin method tests")
    class AuthenticateAdminTests {

        @Test
        @DisplayName("Should successfully authenticate admin and return response")
        void authenticateAdmin_Success() {
            when(adminRepository.findByUsername(loginRequest.getUsername()))
                    .thenReturn(Optional.of(admin));
            when(tokenService.generateToken(admin, claims))
                    .thenReturn(token);

            AdminAuthResponse response = adminAuthenticationService.authenticateAdmin(loginRequest);

            assertNotNull(response);
            assertEquals(token, response.getToken());
            assertEquals(admin.getId(), response.getAdminId());
            assertEquals(admin.getUsername(), response.getUsername());
            assertEquals(admin.getName(), response.getName());

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("Should throw BadCredentialsException when authentication fails")
        void authenticateAdmin_AuthenticationFailure() {
            when(authenticationManager.authenticate(any()))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            AuthenticationException exception = assertThrows(BadCredentialsException.class, () ->
                    adminAuthenticationService.authenticateAdmin(loginRequest)
            );

            assertEquals("Invalid credentials", exception.getMessage());
        }

        @Test
        @DisplayName("Should handle unexpected exceptions during authentication")
        void authenticateAdmin_UnexpectedError() {
            when(authenticationManager.authenticate(any()))
                    .thenThrow(new RuntimeException("Unexpected error"));

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    adminAuthenticationService.authenticateAdmin(loginRequest)
            );

            assertEquals("Authentication failed", exception.getMessage());
        }
    }
}