package rizzerve.authservice.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.client.TableServiceClient;
import rizzerve.authservice.security.service.ServiceAccount;
import rizzerve.authservice.security.token.TokenService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TableServiceIntegration {
    private final TokenService tokenService;
    private final TableServiceClient tableServiceClient;

    public String generateTableServiceToken() {
        ServiceAccount serviceAccount = new ServiceAccount("table-service");

        return tokenService.generateToken(
                serviceAccount,
                Map.of(
                        "service", "auth-service",
                        "scope", "table-operations",
                        "iat", System.currentTimeMillis()
                )
        );
    }

    public boolean isTableAvailable(Integer tableNumber) {
        String token = generateTableServiceToken();

        var response = tableServiceClient.checkTableAvailability(
                tableNumber,
                "Bearer " + token
        );

        return response.getBody() != null && response.getBody().isAvailable();
    }

    public void updateTableStatus(Integer tableNumber, String status) {
        String token = generateTableServiceToken();

        tableServiceClient.updateTableStatus(
                tableNumber,
                status,
                "Bearer " + token
        );
    }
}