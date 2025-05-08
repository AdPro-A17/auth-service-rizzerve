package rizzerve.authservice.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import rizzerve.authservice.dto.AuthResponse;
import rizzerve.authservice.dto.LoginRequest;
import rizzerve.authservice.exception.UserNotFoundException;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;
import rizzerve.authservice.security.token.TokenService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void authenticateUserShouldReturnValidResponse() {
        String username = "test@example.com";
        String password = "password";
        UUID userId = UUID.randomUUID();
        String name = "Test User";
        String token = "jwt.token.here";

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        User user = User.builder()
                .id(userId)
                .username(username)
                .name(name)
                .role(Role.CUSTOMER)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(tokenService.generateToken(eq(user), any())).thenReturn(token);

        AuthResponse response = authenticationService.authenticateUser(request);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(userId, response.getUserId());
        assertEquals(username, response.getUsername());
        assertEquals(name, response.getName());
        assertEquals(Role.CUSTOMER, response.getRole());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        verify(userRepository).findByUsername(username);
        verify(tokenService).generateToken(eq(user), any());
    }

    @Test
    void authenticateUserShouldThrowExceptionWhenUserNotFound() {
        String username = "test@example.com";
        String password = "password";

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                authenticationService.authenticateUser(request)
        );

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        verify(userRepository).findByUsername(username);
        verifyNoInteractions(tokenService);
    }
}