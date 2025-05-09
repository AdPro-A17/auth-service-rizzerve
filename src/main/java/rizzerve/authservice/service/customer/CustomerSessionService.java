package rizzerve.authservice.service.customer;

import rizzerve.authservice.dto.customer.CustomerSessionRequest;
import rizzerve.authservice.dto.customer.CustomerSessionResponse;
import rizzerve.authservice.dto.customer.EndSessionRequest;

public interface CustomerSessionService {
    CustomerSessionResponse createSession(CustomerSessionRequest request);
    void endSession(EndSessionRequest request);
    boolean validateSession(String sessionToken);
    Integer getTableNumberFromSession(String sessionToken);
}