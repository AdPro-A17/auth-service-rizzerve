package rizzerve.authservice.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rizzerve.authservice.dto.admin.AdminProfileResponse;
import rizzerve.authservice.model.Admin;

@Service
@RequiredArgsConstructor
public class AdminProfileServiceImpl implements AdminProfileService {

    @Override
    public AdminProfileResponse getAdminProfile(Admin admin) {
        return AdminProfileResponse.builder()
                .username(admin.getUsername())
                .name(admin.getName())
                .build();
    }
}