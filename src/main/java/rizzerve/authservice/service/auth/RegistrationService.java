package rizzerve.authservice.service.auth;

import rizzerve.authservice.dto.AuthResponse;
import rizzerve.authservice.dto.RegisterRequest;

public interface RegistrationService {
    AuthResponse registerUser(RegisterRequest request);
}