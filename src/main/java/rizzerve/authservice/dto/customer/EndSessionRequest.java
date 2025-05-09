package rizzerve.authservice.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndSessionRequest {
    @NotBlank(message = "Session token is required")
    private String sessionToken;
}
