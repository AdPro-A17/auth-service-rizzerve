package rizzerve.authservice.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSessionResponse {
    private String status;
    private String sessionToken;
    private Integer tableNumber;
    private LocalDateTime startTime;
}