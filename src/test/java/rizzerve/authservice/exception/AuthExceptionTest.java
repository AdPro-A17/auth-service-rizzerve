package rizzerve.authservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AuthExceptionTest {

    @InjectMocks
    private AuthException authException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleRuntimeException() {
        RuntimeException ex = new RuntimeException("Test runtime exception");
        ResponseEntity<Map<String, String>> response = authException.handleRuntimeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Test runtime exception", response.getBody().get("error"));
    }

    @Test
    void handleBadCredentialsException() {
        ResponseEntity<Map<String, String>> response = authException.handleBadCredentialsException();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Invalid username or password", response.getBody().get("error"));
    }

    @Test
    void handleValidationExceptions() {
        FieldError fieldError1 = new FieldError("registerRequest", "username", "Username must be at least 4 characters");
        FieldError fieldError2 = new FieldError("registerRequest", "password", "Password must be at least 6 characters");

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<Map<String, String>> response = authException.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Username must be at least 4 characters", response.getBody().get("username"));
        assertEquals("Password must be at least 6 characters", response.getBody().get("password"));
    }
}