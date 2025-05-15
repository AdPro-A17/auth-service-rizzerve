package rizzerve.authservice.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDetails {
    private String id;
    private int nomorMeja;
    private String status;
}