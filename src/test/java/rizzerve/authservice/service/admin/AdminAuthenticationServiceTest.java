package rizzerve.authservice.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;
import rizzerve.authservice.security.token.TokenService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminAuthenticationServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AdminAuthenticationServiceImpl adminAuthenticationService;

    private AdminLoginRequest loginRequest;
    Admin admin;
    private final UUID adminId = UUID.randomUUID();
    final String token = "test-token";

    @BeforeEach
    void setUp() {
        loginRequest = new AdminLoginRequest("testuser", "password");

        admin = Admin.builder()
                .id(adminId)
                .username("testuser")
                .name("Test User")
                .password("hashedPassword")
                .build();
    }

//    @Test
//    void authenticateAdmin_Success() {
//        when(adminRepository.findByUsername(anyString())).thenReturn(Optional.of(admin));
//        when(tokenService.generateToken(any(Admin.class), anyMap())).thenReturn(token);
//
//        AdminAuthResponse response = adminAuthenticationService.authenticateAdmin(loginRequest);
//
//        assertNotNull(response);
//        assertEquals(admin.getId(), response.getAdminId());
//        assertEquals(admin.getUsername(), response.getUsername());
//        assertEquals(admin.getName(), response.getName());
//        assertEquals(token, response.getToken());
//
//        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(adminRepository).findByUsername(loginRequest.getUsername());
//        verify(tokenService).generateToken(eq(admin), anyMap());
//    }

    @Test
    void authenticateAdmin_UserNotFound() {
        when(adminRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            adminAuthenticationService.authenticateAdmin(loginRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(adminRepository).findByUsername(loginRequest.getUsername());
        verify(tokenService, never()).generateToken(any(), anyMap());
    }
}