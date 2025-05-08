package rizzerve.authservice.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.ProfileRequest;
import rizzerve.authservice.dto.ProfileResponse;
import rizzerve.authservice.model.User;
import rizzerve.authservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;

    @Override
    public ProfileResponse updateProfile(ProfileRequest request, User user) {
        user.setName(request.getName());
        userRepository.save(user);

        return ProfileResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}