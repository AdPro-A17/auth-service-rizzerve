package rizzerve.authservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(String message, int status, LocalDateTime timestamp, Map<String, String> errors) {
        super(message, status, timestamp);
        this.errors = errors;
    }
}