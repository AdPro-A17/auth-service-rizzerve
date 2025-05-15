package rizzerve.authservice.service.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import rizzerve.authservice.client.TableServiceClient;
import rizzerve.authservice.client.TableUpdateResponse;
import rizzerve.authservice.security.token.TokenService;
import rizzerve.authservice.client.TableAvailabilityResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TableServiceIntegrationTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private TableServiceClient tableServiceClient;

    @InjectMocks
    private TableServiceIntegration tableServiceIntegration;

    private Integer tableNumber;
    private String token;

    @BeforeEach
    void setUp() {
        tableNumber = 5;
        token = "service-token";
    }

    @Test
    void generateTableServiceToken_ShouldReturnToken() {
        when(tokenService.generateToken(any(), any())).thenReturn(token);

        String result = tableServiceIntegration.generateTableServiceToken();

        assertEquals(token, result);
        verify(tokenService).generateToken(any(), any(Map.class));
    }

    @Test
    void isTableAvailable_ShouldReturnTrue_WhenTableIsAvailable() {
        when(tokenService.generateToken(any(), any())).thenReturn(token);

        TableAvailabilityResponse availabilityResponse = new TableAvailabilityResponse(true, "Available");
        ResponseEntity<TableAvailabilityResponse> responseEntity = ResponseEntity.ok(availabilityResponse);

        when(tableServiceClient.checkTableAvailability(eq(tableNumber), eq("Bearer " + token)))
                .thenReturn(responseEntity);

        boolean result = tableServiceIntegration.isTableAvailable(tableNumber);

        assertTrue(result);
    }

    @Test
    void isTableAvailable_ShouldReturnFalse_WhenTableIsNotAvailable() {
        when(tokenService.generateToken(any(), any())).thenReturn(token);

        TableAvailabilityResponse availabilityResponse = new TableAvailabilityResponse(false, "Not available");
        ResponseEntity<TableAvailabilityResponse> responseEntity = ResponseEntity.ok(availabilityResponse);

        when(tableServiceClient.checkTableAvailability(eq(tableNumber), eq("Bearer " + token)))
                .thenReturn(responseEntity);

        boolean result = tableServiceIntegration.isTableAvailable(tableNumber);

        assertFalse(result);
    }

    @Test
    void updateTableStatus_ShouldCallTableService() {
        String status = "TERPAKAI";
        when(tokenService.generateToken(any(), any())).thenReturn(token);

        TableUpdateResponse updateResponse = new TableUpdateResponse();
        updateResponse.setStatus("success");
        ResponseEntity<TableUpdateResponse> responseEntity = ResponseEntity.ok(updateResponse);

        when(tableServiceClient.updateTableStatus(eq(tableNumber), eq(status), eq("Bearer " + token)))
                .thenReturn(responseEntity);

        tableServiceIntegration.updateTableStatus(tableNumber, status);

        verify(tableServiceClient).updateTableStatus(eq(tableNumber), eq(status), eq("Bearer " + token));
    }
}
