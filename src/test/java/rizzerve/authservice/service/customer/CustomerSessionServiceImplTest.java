package rizzerve.authservice.service.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.customer.CustomerSessionRequest;
import rizzerve.authservice.dto.customer.CustomerSessionResponse;
import rizzerve.authservice.dto.customer.EndSessionRequest;
import rizzerve.authservice.exception.SessionNotFoundException;
import rizzerve.authservice.exception.TableAlreadyOccupiedException;
import rizzerve.authservice.exception.TableNotFoundException;
import rizzerve.authservice.security.token.TokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerSessionServiceImplTest {

    @Mock
    private CustomerSessionRepository sessionRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private TableServiceIntegration tableServiceIntegration;

    @InjectMocks
    private CustomerSessionServiceImpl customerSessionService;

    private CustomerSessionRequest sessionRequest;
    private CustomerSession customerSession;
    private String sessionToken;
    private UUID sessionId;
    private Integer tableNumber;

    @BeforeEach
    void setUp() {
        tableNumber = 5;
        sessionToken = "test-session-token";
        sessionId = UUID.randomUUID();

        sessionRequest = new CustomerSessionRequest();
        sessionRequest.setTableNumber(tableNumber);

        customerSession = CustomerSession.builder()
                .id(sessionId)
                .tableNumber(tableNumber)
                .sessionToken(sessionToken)
                .startTime(LocalDateTime.now())
                .active(true)
                .build();
    }

    @Test
    void createSession_ShouldSucceed_WhenTableIsAvailable() {
        when(sessionRepository.findByTableNumberAndActive(tableNumber, true))
                .thenReturn(Optional.empty());
        when(tableServiceIntegration.isTableAvailable(tableNumber)).thenReturn(true);
        when(tokenService.generateSessionToken(tableNumber)).thenReturn(sessionToken);
        when(sessionRepository.save(any(CustomerSession.class))).thenReturn(customerSession);

        CustomerSessionResponse response = customerSessionService.createSession(sessionRequest);

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals(sessionId, response.getSessionId());
        assertEquals(sessionToken, response.getSessionToken());
        assertEquals(tableNumber, response.getTableNumber());

        verify(tableServiceIntegration).updateTableStatus(tableNumber, "TERPAKAI");
        verify(sessionRepository).save(any(CustomerSession.class));
    }

    @Test
    void createSession_ShouldThrowException_WhenTableIsAlreadyOccupied() {
        when(sessionRepository.findByTableNumberAndActive(tableNumber, true))
                .thenReturn(Optional.of(customerSession));

        assertThrows(TableAlreadyOccupiedException.class, () ->
                customerSessionService.createSession(sessionRequest)
        );

        verify(tableServiceIntegration, never()).isTableAvailable(any());
        verify(tableServiceIntegration, never()).updateTableStatus(any(), any());
    }

    @Test
    void createSession_ShouldThrowException_WhenTableIsNotAvailable() {
        when(sessionRepository.findByTableNumberAndActive(tableNumber, true))
                .thenReturn(Optional.empty());
        when(tableServiceIntegration.isTableAvailable(tableNumber)).thenReturn(false);

        assertThrows(TableNotFoundException.class, () ->
                customerSessionService.createSession(sessionRequest)
        );

        verify(tableServiceIntegration, never()).updateTableStatus(any(), any());
    }

    @Test
    void endSession_ShouldSucceed_WhenSessionExists() {
        EndSessionRequest endRequest = new EndSessionRequest();
        endRequest.setSessionToken(sessionToken);

        when(sessionRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(customerSession));

        customerSessionService.endSession(endRequest);

        verify(sessionRepository).save(any(CustomerSession.class));
        verify(tableServiceIntegration).updateTableStatus(tableNumber, "TERSEDIA");

        assertFalse(customerSession.isActive());
        assertNotNull(customerSession.getEndTime());
    }

    @Test
    void endSession_ShouldThrowException_WhenSessionDoesNotExist() {
        EndSessionRequest endRequest = new EndSessionRequest();
        endRequest.setSessionToken("non-existent-token");

        when(sessionRepository.findBySessionToken("non-existent-token"))
                .thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () ->
                customerSessionService.endSession(endRequest)
        );

        verify(sessionRepository, never()).save(any());
        verify(tableServiceIntegration, never()).updateTableStatus(any(), any());
    }

    @Test
    void validateSession_ShouldReturnTrue_WhenSessionIsValid() {
        when(tokenService.validateSessionToken(sessionToken)).thenReturn(true);
        when(sessionRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(customerSession));

        boolean result = customerSessionService.validateSession(sessionToken);

        assertTrue(result);
    }

    @Test
    void validateSession_ShouldReturnFalse_WhenTokenIsInvalid() {
        when(tokenService.validateSessionToken(sessionToken)).thenReturn(false);

        boolean result = customerSessionService.validateSession(sessionToken);

        assertFalse(result);
        verify(sessionRepository, never()).findBySessionToken(any());
    }

    @Test
    void validateSession_ShouldReturnFalse_WhenSessionDoesNotExist() {
        when(tokenService.validateSessionToken(sessionToken)).thenReturn(true);
        when(sessionRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.empty());

        boolean result = customerSessionService.validateSession(sessionToken);

        assertFalse(result);
    }

    @Test
    void validateSession_ShouldReturnFalse_WhenSessionIsInactive() {
        customerSession.setActive(false);
        when(tokenService.validateSessionToken(sessionToken)).thenReturn(true);
        when(sessionRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(customerSession));

        boolean result = customerSessionService.validateSession(sessionToken);

        assertFalse(result);
    }

    @Test
    void getTableNumberFromSession_ShouldReturnTableNumber_WhenSessionIsValid() {
        when(tokenService.validateSessionToken(sessionToken)).thenReturn(true);
        when(sessionRepository.findBySessionToken(sessionToken))
                .thenReturn(Optional.of(customerSession));
        when(tokenService.extractTableNumber(sessionToken)).thenReturn(tableNumber);

        Integer result = customerSessionService.getTableNumberFromSession(sessionToken);

        assertNotNull(result);
        assertEquals(tableNumber, result);
    }

    @Test
    void getTableNumberFromSession_ShouldReturnNull_WhenSessionIsInvalid() {
        when(tokenService.validateSessionToken(sessionToken)).thenReturn(false);

        Integer result = customerSessionService.getTableNumberFromSession(sessionToken);

        assertNull(result);
        verify(tokenService, never()).extractTableNumber(any());
    }
}
