package rizzerve.authservice.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableUpdateResponse {
    private String status;
    private TableDetails table;
}