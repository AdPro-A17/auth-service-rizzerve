package rizzerve.authservice.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableAvailabilityResponse {
    private boolean available;
    private String message;
}