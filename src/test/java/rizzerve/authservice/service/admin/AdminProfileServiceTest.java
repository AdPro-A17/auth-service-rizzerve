package rizzerve.authservice.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.model.Admin;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AdminProfileServiceTest {

    @InjectMocks
    private AdminProfileServiceImpl adminProfileService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .name("Test User")
                .password("hashedPassword")
                .build();
    }

    @Test
    void getAdminProfile_Success() {
        AdminProfileResponse response = adminProfileService.getAdminProfile(admin);

        assertNotNull(response);
        assertEquals(admin.getUsername(), response.getUsername());
        assertEquals(admin.getName(), response.getName());
    }
}