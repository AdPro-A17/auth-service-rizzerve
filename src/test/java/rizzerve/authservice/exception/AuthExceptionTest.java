package rizzerve.authservice.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class AuthExceptionTest {

    private AutoCloseable closeable;
    private AuthException authException;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        authException = new AuthException();
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void handleRuntimeException_ReturnsErrorMap() {
        String errorMessage = "Test error message";
        RuntimeException runtimeException = new RuntimeException(errorMessage);

        ResponseEntity<Map<String, String>> response = authException.handleRuntimeException(runtimeException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void handleBadCredentialsException_ReturnsUnauthorized() {
        ResponseEntity<Map<String, String>> response = authException.handleBadCredentialsException();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid username or password", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void handleValidationExceptions_ReturnsMappedErrors() {
        FieldError fieldError = new FieldError("object", "field", "error message");
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Map<String, String>> response = authException.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error message", Objects.requireNonNull(response.getBody()).get("field"));
    }

    @Test
    void handleValidationExceptions_WithMultipleErrors_ReturnsMappedErrors() {
        FieldError fieldError1 = new FieldError("object", "field1", "error message 1");
        FieldError fieldError2 = new FieldError("object", "field2", "error message 2");
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError1, fieldError2));

        ResponseEntity<Map<String, String>> response = authException.handleValidationExceptions(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error message 1", Objects.requireNonNull(response.getBody()).get("field1"));
        assertEquals("error message 2", Objects.requireNonNull(response.getBody()).get("field2"));
    }
}