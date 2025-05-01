package rizzerve.authservice.service;

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

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    private final String TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequest = RegisterRequest.builder()
                .name("Test User")
                .username("testuser")
                .password("password123")
                .role(Role.CUSTOMER)
                .build();

        loginRequest = LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .username("testuser")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn(TOKEN);
    }

    @Test
    void registerSuccess() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        AuthResponse response = authService.register(registerRequest);

        verify(userRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));

        assertEquals(TOKEN, response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getName());
        assertEquals(Role.CUSTOMER, response.getRole());
    }

    @Test
    void registerWithNullRole() {
        registerRequest.setRole(null);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        AuthResponse response = authService.register(registerRequest);

        verify(userRepository).save(any(User.class));
        assertEquals(Role.CUSTOMER, response.getRole());
    }

    @Test
    void registerUsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void loginSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        AuthResponse response = authService.login(loginRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password123")
        );
        verify(userRepository).findByUsername("testuser");
        verify(jwtService).generateToken(user);

        assertEquals(TOKEN, response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("Test User", response.getName());
        assertEquals(Role.CUSTOMER, response.getRole());
    }

    @Test
    void loginUserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password123")
        );
        verify(userRepository).findByUsername("testuser");

        assertEquals("User not found", exception.getMessage());
    }
}