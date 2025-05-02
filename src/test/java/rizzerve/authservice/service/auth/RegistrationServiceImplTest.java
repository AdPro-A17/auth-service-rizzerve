package rizzerve.authservice.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rizzerve.authservice.dto.AuthResponse;
import rizzerve.authservice.dto.RegisterRequest;
import rizzerve.authservice.exception.UserAlreadyExistsException;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;
import rizzerve.authservice.security.token.TokenService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void registerUserShouldReturnValidResponseWithSpecifiedRole() {
        String username = "test@example.com";
        String password = "password";
        String encodedPassword = "encoded_password";
        String name = "Test User";
        Role role = Role.ADMIN;
        String token = "jwt.token.here";

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setName(name);
        request.setRole(role);

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password(encodedPassword)
                .name(name)
                .role(role)
                .build();

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenService.generateToken(eq(savedUser), any())).thenReturn(token);

        AuthResponse response = registrationService.registerUser(request);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(savedUser.getId(), response.getUserId());
        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
        assertEquals(role, response.getRole());

        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
        verify(tokenService).generateToken(eq(savedUser), any());
    }

    @Test
    void registerUserShouldUseDefaultRoleWhenRoleIsNull() {
        // Arrange
        String username = "test@example.com";
        String password = "password";
        String encodedPassword = "encoded_password";
        String name = "Test User";
        String token = "jwt.token.here";

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setName(name);
        request.setRole(null);

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password(encodedPassword)
                .name(name)
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenService.generateToken(eq(savedUser), any())).thenReturn(token);

        AuthResponse response = registrationService.registerUser(request);

        assertNotNull(response);
        assertEquals(Role.CUSTOMER, response.getRole());

        verify(userRepository).save(argThat(user ->
                user.getRole() == Role.CUSTOMER
        ));
    }

    @Test
    void registerUserShouldThrowExceptionWhenUsernameExists() {
        String username = "test@example.com";

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
                registrationService.registerUser(request)
        );

        verify(userRepository).existsByUsername(username);
        verifyNoMoreInteractions(userRepository, passwordEncoder, tokenService);
    }
}