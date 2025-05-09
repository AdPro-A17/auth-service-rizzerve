package rizzerve.authservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rizzerve.authservice.dto.customer.CustomerSessionRequest;
import rizzerve.authservice.dto.customer.CustomerSessionResponse;
import rizzerve.authservice.dto.customer.EndSessionRequest;
import rizzerve.authservice.service.customer.CustomerSessionService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerSessionControllerTest {

    @Mock
    private CustomerSessionService customerSessionService;

    @InjectMocks
    private CustomerSessionController controller;

    private CustomerSessionRequest sessionRequest;
    private CustomerSessionResponse sessionResponse;
    private EndSessionRequest endSessionRequest;
    private String sessionToken;
    private Integer tableNumber;

    @BeforeEach
    void setUp() {
        tableNumber = 5;
        sessionToken = "test-session-token";

        sessionRequest = new CustomerSessionRequest();
        sessionRequest.setTableNumber(tableNumber);

        sessionResponse = CustomerSessionResponse.builder()
                .status("success")
                .sessionId(UUID.randomUUID())
                .sessionToken(sessionToken)
                .tableNumber(tableNumber)
                .startTime(LocalDateTime.now())
                .build();

        endSessionRequest = new EndSessionRequest();
        endSessionRequest.setSessionToken(sessionToken);
    }

    @Test
    void createSession_ShouldReturnSuccessResponse() {
        when(customerSessionService.createSession(any(CustomerSessionRequest.class)))
                .thenReturn(sessionResponse);

        ResponseEntity<CustomerSessionResponse> response = controller.createSession(sessionRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionResponse, response.getBody());
        verify(customerSessionService).createSession(sessionRequest);
    }

    @Test
    void endSession_ShouldReturnSuccessResponse() {
        doNothing().when(customerSessionService).endSession(any(EndSessionRequest.class));

        ResponseEntity<Void> response = controller.endSession(endSessionRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerSessionService).endSession(endSessionRequest);
    }

    @Test
    void validateSession_ShouldReturnTrue_WhenSessionIsValid() {
        when(customerSessionService.validateSession(sessionToken)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.validateSession(sessionToken);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(customerSessionService).validateSession(sessionToken);
    }

    @Test
    void validateSession_ShouldReturnFalse_WhenSessionIsInvalid() {
        when(customerSessionService.validateSession(sessionToken)).thenReturn(false);

        ResponseEntity<Boolean> response = controller.validateSession(sessionToken);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
        verify(customerSessionService).validateSession(sessionToken);
    }

    @Test
    void getTableNumber_ShouldReturnTableNumber_WhenSessionIsValid() {
        when(customerSessionService.getTableNumberFromSession(sessionToken)).thenReturn(tableNumber);

        ResponseEntity<Integer> response = controller.getTableNumber(sessionToken);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tableNumber, response.getBody());
        verify(customerSessionService).getTableNumberFromSession(sessionToken);
    }

    @Test
    void getTableNumber_ShouldReturnNotFound_WhenSessionIsInvalid() {
        when(customerSessionService.getTableNumberFromSession(sessionToken)).thenReturn(null);

        ResponseEntity<Integer> response = controller.getTableNumber(sessionToken);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerSessionService).getTableNumberFromSession(sessionToken);
    }
}
