package rizzerve.authservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import rizzerve.authservice.dto.AuthResponse;
import rizzerve.authservice.dto.LoginRequest;
import rizzerve.authservice.dto.RegisterRequest;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;
import rizzerve.authservice.security.JwtService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void register_WithValidData_ReturnsAuthResponse() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .username("testuser")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .username(request.getUsername())
                .password("encodedPassword")
                .role(request.getRole())
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(request.getUsername(), response.getUsername());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getRole(), response.getRole());
        assertEquals("jwtToken", response.getToken());

        verify(userRepository).existsByUsername(request.getUsername());
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void register_WithExistingUsername_ThrowsException() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .username("existinguser")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).existsByUsername(request.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_WithNullRole_DefaultsToCustomer() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .username("testuser")
                .password("password")
                .role(null)
                .build();

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .username(request.getUsername())
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(Role.CUSTOMER, response.getRole());
    }

    @Test
    void login_WithValidCredentials_ReturnsAuthResponse() {
        LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("password")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .username(request.getUsername())
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getRole(), response.getRole());
        assertEquals("jwtToken", response.getToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        verify(userRepository).findByUsername(request.getUsername());
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_WithNonExistentUser_ThrowsException() {
        LoginRequest request = LoginRequest.builder()
                .username("nonexistentuser")
                .password("password")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any())).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("User not found", exception.getMessage());
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByUsername(request.getUsername());
    }
}