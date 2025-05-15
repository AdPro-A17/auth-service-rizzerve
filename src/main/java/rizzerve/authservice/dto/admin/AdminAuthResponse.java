package rizzerve.authservice.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAuthResponse {
    private String token;
    private UUID adminId;
    private String username;
    private String name;
}