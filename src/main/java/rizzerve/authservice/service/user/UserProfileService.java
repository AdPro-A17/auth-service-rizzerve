package rizzerve.authservice.service.user;

import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.model.User;

public interface UserProfileService {
    ProfileResponse updateProfile(ProfileRequest request, User user);
}