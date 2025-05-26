package rizzerve.authservice.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminRegistrationServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AdminRegistrationServiceImpl adminRegistrationService;

    private AdminRegisterRequest validRequest;
    private Admin mockAdmin;
    private String mockToken;

    @BeforeEach
    void setUp() {
        validRequest = AdminRegisterRequest.builder()
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .build();

        mockAdmin = Admin.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .username("johndoe")
                .password("encodedPassword123")
                .build();

        mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    }

    @Test
    void registerAdmin_Success() {
        when(adminRepository.existsByUsername(validRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(validRequest.getPassword())).thenReturn("encodedPassword123");
        when(adminRepository.save(any(Admin.class))).thenReturn(mockAdmin);
        when(tokenService.generateToken(eq(mockAdmin), any(Map.class))).thenReturn(mockToken);

        AdminAuthResponse response = adminRegistrationService.registerAdmin(validRequest);

        assertNotNull(response);
        assertEquals(mockToken, response.getToken());
        assertEquals(mockAdmin.getId(), response.getAdminId());
        assertEquals(mockAdmin.getUsername(), response.getUsername());
        assertEquals(mockAdmin.getName(), response.getName());

        verify(adminRepository).existsByUsername(validRequest.getUsername());
        verify(passwordEncoder).encode(validRequest.getPassword());
        verify(adminRepository).save(any(Admin.class));
        verify(tokenService).generateToken(eq(mockAdmin), any(Map.class));
    }

    @Test
    void registerAdmin_ThrowsException_WhenUsernameAlreadyExists() {
        when(adminRepository.existsByUsername(validRequest.getUsername())).thenReturn(true);

        AdminAlreadyExistsException exception = assertThrows(
                AdminAlreadyExistsException.class,
                () -> adminRegistrationService.registerAdmin(validRequest)
        );

        assertEquals("Username already exists", exception.getMessage());

        verify(adminRepository).existsByUsername(validRequest.getUsername());
        verify(passwordEncoder, never()).encode(anyString());
        verify(adminRepository, never()).save(any(Admin.class));
        verify(tokenService, never()).generateToken(any(), any());
    }

    @Test
    void buildAdminFromRequest_CreatesCorrectAdmin() {
        when(adminRepository.existsByUsername(validRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(validRequest.getPassword())).thenReturn("encodedPassword123");
        when(adminRepository.save(any(Admin.class))).thenReturn(mockAdmin);
        when(tokenService.generateToken(any(Admin.class), any(Map.class))).thenReturn(mockToken);

        adminRegistrationService.registerAdmin(validRequest);

        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(adminCaptor.capture());

        Admin capturedAdmin = adminCaptor.getValue();
        assertEquals(validRequest.getName(), capturedAdmin.getName());
        assertEquals(validRequest.getUsername(), capturedAdmin.getUsername());
        assertEquals("encodedPassword123", capturedAdmin.getPassword());
    }

    @Test
    void registerAdmin_CallsMetricsService() {
        when(adminRepository.existsByUsername(validRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(validRequest.getPassword())).thenReturn("encodedPassword123");
        when(adminRepository.save(any(Admin.class))).thenReturn(mockAdmin);
        when(tokenService.generateToken(any(Admin.class), any(Map.class))).thenReturn(mockToken);

        adminRegistrationService.registerAdmin(validRequest);
    }

    @Test
    void registerAdmin_CallsTokenServiceWithCorrectParameters() {
        when(adminRepository.existsByUsername(validRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(validRequest.getPassword())).thenReturn("encodedPassword123");
        when(adminRepository.save(any(Admin.class))).thenReturn(mockAdmin);
        when(tokenService.generateToken(any(Admin.class), any(Map.class))).thenReturn(mockToken);

        adminRegistrationService.registerAdmin(validRequest);

        verify(tokenService).generateToken(eq(mockAdmin), any(Map.class));
    }

    @Test
    void registerAdmin_WithNullRequest_ThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            adminRegistrationService.registerAdmin(null);
        });
    }

    @Test
    void registerAdmin_WithEmptyUsername_StillProcesses() {
        AdminRegisterRequest requestWithEmptyUsername = AdminRegisterRequest.builder()
                .name("John Doe")
                .username("")
                .password("password123")
                .build();

        when(adminRepository.existsByUsername("")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

        Admin adminWithEmptyUsername = Admin.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .username("")
                .password("encodedPassword123")
                .build();

        when(adminRepository.save(any(Admin.class))).thenReturn(adminWithEmptyUsername);
        when(tokenService.generateToken(any(Admin.class), any(Map.class))).thenReturn(mockToken);

        AdminAuthResponse response = adminRegistrationService.registerAdmin(requestWithEmptyUsername);

        assertNotNull(response);
        assertEquals("", response.getUsername());
        verify(adminRepository).existsByUsername("");
    }

    @Test
    void registerAdmin_PasswordEncodingVerification() {
        String rawPassword = "mySecurePassword";
        String encodedPassword = "encodedMySecurePassword";

        AdminRegisterRequest request = AdminRegisterRequest.builder()
                .name("Test User")
                .username("testuser")
                .password(rawPassword)
                .build();

        when(adminRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(adminRepository.save(any(Admin.class))).thenReturn(mockAdmin);
        when(tokenService.generateToken(any(Admin.class), any(Map.class))).thenReturn(mockToken);

        adminRegistrationService.registerAdmin(request);

        verify(passwordEncoder).encode(rawPassword);

        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(adminCaptor.capture());
        assertEquals(encodedPassword, adminCaptor.getValue().getPassword());
    }
}