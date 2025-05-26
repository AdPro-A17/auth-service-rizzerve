package rizzerve.authservice.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.dto.admin.AdminUpdateNameRequest;
import rizzerve.authservice.exception.AdminNotFoundException;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.repository.AdminRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminManagementServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminManagementServiceImpl adminManagementService;

    private Admin testAdmin;
    private AdminUpdateNameRequest validUpdateRequest;

    @BeforeEach
    void setUp() {
        testAdmin = Admin.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .name("Test User")
                .password("hashedpassword")
                .build();

        validUpdateRequest = AdminUpdateNameRequest.builder()
                .name("Updated Name")
                .build();
    }

    @Test
    void updateAdminName_ShouldReturnUpdatedProfile_WhenAdminExists() {
        Admin updatedAdmin = Admin.builder()
                .id(testAdmin.getId())
                .username(testAdmin.getUsername())
                .name("Updated Name")
                .password(testAdmin.getPassword())
                .build();

        when(adminRepository.findById(testAdmin.getId())).thenReturn(Optional.of(testAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(updatedAdmin);

        AdminProfileResponse result = adminManagementService.updateAdminName(testAdmin, validUpdateRequest);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getUsername()).isEqualTo(testAdmin.getUsername());
        verify(adminRepository).findById(testAdmin.getId());
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void updateAdminName_ShouldThrowException_WhenAdminNotFound() {
        when(adminRepository.findById(testAdmin.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminManagementService.updateAdminName(testAdmin, validUpdateRequest))
                .isInstanceOf(AdminNotFoundException.class)
                .hasMessage("Admin not found");

        verify(adminRepository).findById(testAdmin.getId());
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @ParameterizedTest(name = "Should fail validation when name is: {0}")
    @MethodSource("invalidNameProvider")
    void updateAdminName_ShouldFailValidation_WhenNameIsInvalid(String description, String invalidName) {
        AdminUpdateNameRequest invalidRequest = AdminUpdateNameRequest.builder()
                .name(invalidName)
                .build();

        // Assuming validation happens before repository call, adjust based on your actual implementation
        assertThatThrownBy(() -> adminManagementService.updateAdminName(testAdmin, invalidRequest))
                .isInstanceOf(IllegalArgumentException.class) // or ValidationException, depending on your implementation
                .hasMessageContaining("name"); // adjust message based on your validation

        // Verify repository methods are never called when validation fails
        verify(adminRepository, never()).findById(any());
        verify(adminRepository, never()).save(any());
    }

    private static Stream<Arguments> invalidNameProvider() {
        return Stream.of(
                Arguments.of("null", null),
                Arguments.of("empty string", ""),
                Arguments.of("blank string", "   ")
        );
    }

    @Test
    void deleteAdminAccount_ShouldDeleteAdmin_WhenAdminExists() {
        when(adminRepository.findById(testAdmin.getId())).thenReturn(Optional.of(testAdmin));

        adminManagementService.deleteAdminAccount(testAdmin);

        verify(adminRepository).findById(testAdmin.getId());
        verify(adminRepository).delete(testAdmin);
    }

    @Test
    void deleteAdminAccount_ShouldThrowException_WhenAdminNotFound() {
        when(adminRepository.findById(testAdmin.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminManagementService.deleteAdminAccount(testAdmin))
                .isInstanceOf(AdminNotFoundException.class)
                .hasMessage("Admin not found");

        verify(adminRepository).findById(testAdmin.getId());
        verify(adminRepository, never()).delete(any(Admin.class));
    }
}