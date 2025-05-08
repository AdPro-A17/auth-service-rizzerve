package rizzerve.authservice.service.auth;

import rizzerve.authservice.dto.AuthResponse;
import rizzerve.authservice.dto.LoginRequest;

public interface AuthenticationService {
    AuthResponse authenticateUser(LoginRequest request);
}