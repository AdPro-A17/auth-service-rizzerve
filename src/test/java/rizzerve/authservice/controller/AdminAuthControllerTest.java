package rizzerve.authservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rizzerve.authservice.dto.admin.AdminAuthResponse;
import rizzerve.authservice.dto.admin.AdminLoginRequest;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.dto.admin.AdminRegisterRequest;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.service.admin.AdminAuthenticationService;
import rizzerve.authservice.service.admin.AdminProfileService;
import rizzerve.authservice.service.admin.AdminRegistrationService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminAuthControllerTest {

    @Mock
    private AdminRegistrationService registrationService;

    @Mock
    private AdminAuthenticationService authenticationService;

    @Mock
    private AdminProfileService profileService;

    @InjectMocks
    private AdminAuthController adminAuthController;

    private AdminRegisterRequest registerRequest;
    private AdminLoginRequest loginRequest;
    private AdminAuthResponse authResponse;
    private AdminProfileResponse profileResponse;
    private Admin admin;

    @BeforeEach
    void setup() {
        UUID id = UUID.randomUUID();

        registerRequest = new AdminRegisterRequest();
        registerRequest.setName("Test Admin");
        registerRequest.setUsername("testadmin");
        registerRequest.setPassword("password123");

        loginRequest = new AdminLoginRequest();
        loginRequest.setUsername("testadmin");
        loginRequest.setPassword("password123");

        authResponse = new AdminAuthResponse();
        authResponse.setToken("test-jwt-token");
        authResponse.setAdminId(id);
        authResponse.setUsername("testadmin");
        authResponse.setName("Test Admin");

        profileResponse = new AdminProfileResponse();
        profileResponse.setUsername("testadmin");
        profileResponse.setName("Test Admin");

        admin = new Admin();
        admin.setId(id);
        admin.setUsername("testadmin");
        admin.setName("Test Admin");
        admin.setPassword("encodedPassword");
    }

    @Test
    void registerShouldReturnAdminAuthResponse() {
        when(registrationService.registerAdmin(any(AdminRegisterRequest.class))).thenReturn(authResponse);

        ResponseEntity<AdminAuthResponse> response = adminAuthController.register(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void loginShouldReturnAdminAuthResponse() {
        when(authenticationService.authenticateAdmin(any(AdminLoginRequest.class))).thenReturn(authResponse);

        ResponseEntity<AdminAuthResponse> response = adminAuthController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
    }

    @Test
    void getProfileShouldReturnAdminProfileResponse() {
        when(profileService.getAdminProfile(any(Admin.class))).thenReturn(profileResponse);

        ResponseEntity<AdminProfileResponse> response = adminAuthController.getProfile(admin);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileResponse, response.getBody());
    }
}