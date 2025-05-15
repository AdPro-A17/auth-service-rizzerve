package rizzerve.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "table-service", url = "${table-service.url}")
public interface TableServiceClient {

    @GetMapping("/api/table/check-availability")
    ResponseEntity<TableAvailabilityResponse> checkTableAvailability(
            @RequestParam("tableNumber") Integer tableNumber,
            @RequestHeader("Authorization") String authToken);

    @PutMapping("/api/table/update-status")
    ResponseEntity<TableUpdateResponse> updateTableStatus(
            @RequestParam("tableNumber") Integer tableNumber,
            @RequestParam("status") String status,
            @RequestHeader("Authorization") String authToken);
}