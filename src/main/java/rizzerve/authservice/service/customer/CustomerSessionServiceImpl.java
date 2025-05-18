package rizzerve.authservice.service.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rizzerve.authservice.dto.customer.CustomerSessionRequest;
import rizzerve.authservice.dto.customer.CustomerSessionResponse;
import rizzerve.authservice.dto.customer.EndSessionRequest;
import rizzerve.authservice.exception.TableNotFoundException;
import rizzerve.authservice.security.token.TokenService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerSessionServiceImpl implements CustomerSessionService {
    private final TokenService tokenService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${table-service.url}")
    private String tableServiceUrl;

    @Override
    public CustomerSessionResponse createSession(CustomerSessionRequest request) {
        Integer tableNumber = request.getTableNumber();

        String checkUrl = tableServiceUrl + "/api/table/check-availability?tableNumber=" + tableNumber;
        ResponseEntity<TableAvailabilityResponse> response = restTemplate.getForEntity(checkUrl, TableAvailabilityResponse.class);

        if (response.getBody() == null || !response.getBody().isAvailable()) {
            throw new TableNotFoundException("Table " + tableNumber + " is not available");
        }

        String updateUrl = tableServiceUrl + "/api/table/update-status?tableNumber=" + tableNumber + "&status=TERPAKAI";
        restTemplate.put(updateUrl, null);

        String sessionToken = tokenService.generateSessionToken(tableNumber);

        return CustomerSessionResponse.builder()
                .status("success")
                .sessionToken(sessionToken)
                .tableNumber(tableNumber)
                .startTime(LocalDateTime.now())
                .build();
    }

    @Override
    public void endSession(EndSessionRequest request) {
        Integer tableNumber = tokenService.extractTableNumber(request.getSessionToken());
        if (tableNumber != null) {
            String updateUrl = tableServiceUrl + "/api/table/update-status?tableNumber=" + tableNumber + "&status=TERSEDIA";
            restTemplate.put(updateUrl, null);
        }
    }

    @Override
    public boolean validateSession(String sessionToken) {
        return tokenService.validateSessionToken(sessionToken);
    }

    @Override
    public Integer getTableNumberFromSession(String sessionToken) {
        if (!validateSession(sessionToken)) {
            return null;
        }
        return tokenService.extractTableNumber(sessionToken);
    }

    private static class TableAvailabilityResponse {
        private boolean available;
        private String message;

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}