package rizzerve.authservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rizzerve.authservice.dto.*;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.service.auth.AuthenticationService;
import rizzerve.authservice.service.auth.RegistrationService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginShouldReturnAuthResponse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password");

        AuthResponse expectedResponse = AuthResponse.builder()
                .token("jwt.token.here")
                .userId(UUID.randomUUID())
                .username("test@example.com")
                .name("Test User")
                .role(Role.CUSTOMER)
                .build();

        when(authenticationService.authenticateUser(loginRequest)).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(authenticationService).authenticateUser(loginRequest);
    }
}