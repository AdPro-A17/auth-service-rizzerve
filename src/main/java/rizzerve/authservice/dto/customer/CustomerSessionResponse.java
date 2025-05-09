package rizzerve.authservice.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSessionResponse {
    private String status;
    private UUID sessionId;
    private String sessionToken;
    private Integer tableNumber;
    private LocalDateTime startTime;
}