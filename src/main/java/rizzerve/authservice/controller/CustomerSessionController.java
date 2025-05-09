package rizzerve.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rizzerve.authservice.dto.customer.CustomerSessionRequest;
import rizzerve.authservice.dto.customer.CustomerSessionResponse;
import rizzerve.authservice.dto.customer.EndSessionRequest;
import rizzerve.authservice.service.customer.CustomerSessionService;

@RestController
@RequestMapping("/api/customer/session")
@RequiredArgsConstructor
public class CustomerSessionController {

    private final CustomerSessionService customerSessionService;

    @PostMapping("/create")
    public ResponseEntity<CustomerSessionResponse> createSession(@Valid @RequestBody CustomerSessionRequest request) {
        CustomerSessionResponse response = customerSessionService.createSession(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endSession(@Valid @RequestBody EndSessionRequest request) {
        customerSessionService.endSession(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateSession(@RequestParam String sessionToken) {
        boolean isValid = customerSessionService.validateSession(sessionToken);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/table-number")
    public ResponseEntity<Integer> getTableNumber(@RequestParam String sessionToken) {
        Integer tableNumber = customerSessionService.getTableNumberFromSession(sessionToken);
        if (tableNumber == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tableNumber);
    }
}