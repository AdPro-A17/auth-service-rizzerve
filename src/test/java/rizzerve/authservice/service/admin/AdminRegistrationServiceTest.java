package rizzerve.authservice.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminRegisterRequest;
import rizzerve.authservice.exception.AdminAlreadyExistsException;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;
import rizzerve.authservice.security.token.TokenService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminRegistrationServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AdminRegistrationServiceImpl adminRegistrationService;

    private AdminRegisterRequest registerRequest;
    private Admin savedAdmin;
    private final UUID adminId = UUID.randomUUID();
    final String token = "test-token";
    private final String encodedPassword = "encodedPassword";

    @BeforeEach
    void setUp() {
        registerRequest = new AdminRegisterRequest("Test User", "testuser", "password");

        savedAdmin = Admin.builder()
                .id(adminId)
                .username("testuser")
                .name("Test User")
                .password(encodedPassword)
                .build();
    }

//    @Test
//    void registerAdmin_Success() {
//        when(adminRepository.existsByUsername(anyString())).thenReturn(false);
//        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
//        when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);
//        when(tokenService.generateToken(any(Admin.class), anyMap())).thenReturn(token);
//
//        AdminAuthResponse response = adminRegistrationService.registerAdmin(registerRequest);
//
//        assertNotNull(response);
//        assertEquals(savedAdmin.getId(), response.getAdminId());
//        assertEquals(savedAdmin.getUsername(), response.getUsername());
//        assertEquals(savedAdmin.getName(), response.getName());
//        assertEquals(token, response.getToken());
//
//        verify(adminRepository).existsByUsername(registerRequest.getUsername());
//        verify(passwordEncoder).encode(registerRequest.getPassword());
//        verify(adminRepository).save(any(Admin.class));
//        verify(tokenService).generateToken(eq(savedAdmin), anyMap());
//    }

    @Test
    void registerAdmin_UsernameExists() {
        when(adminRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(AdminAlreadyExistsException.class, () ->
                adminRegistrationService.registerAdmin(registerRequest)
        );

        verify(adminRepository).existsByUsername(registerRequest.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(adminRepository, never()).save(any(Admin.class));
        verify(tokenService, never()).generateToken(any(), anyMap());
    }
}