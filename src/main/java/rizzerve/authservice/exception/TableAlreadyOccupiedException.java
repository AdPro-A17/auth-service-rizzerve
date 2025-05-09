package rizzerve.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TableAlreadyOccupiedException extends RuntimeException {
    public TableAlreadyOccupiedException(String message) {
        super(message);
    }
}