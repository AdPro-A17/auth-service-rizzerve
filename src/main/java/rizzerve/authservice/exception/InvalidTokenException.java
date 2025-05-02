package rizzerve.authservice.exception;

public class InvalidTokenException extends AuthServiceException {
    public InvalidTokenException(String message) {
        super(message);
    }
}