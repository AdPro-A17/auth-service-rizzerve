package rizzerve.authservice.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void updateProfileShouldUpdateNameAndReturnCorrectResponse() {
        UUID userId = UUID.randomUUID();
        String username = "test@example.com";
        String oldName = "Old Name";
        String newName = "New Name";
        Role role = Role.CUSTOMER;

        User user = User.builder()
                .id(userId)
                .username(username)
                .name(oldName)
                .role(role)
                .build();

        ProfileRequest request = new ProfileRequest();
        request.setName(newName);

        ProfileResponse response = userProfileService.updateProfile(request, user);

        assertEquals(newName, user.getName());
        assertNotNull(response);
        assertEquals(username, response.getUsername());
        assertEquals(newName, response.getName());
        assertEquals(role, response.getRole());

        verify(userRepository).save(user);
    }
}
