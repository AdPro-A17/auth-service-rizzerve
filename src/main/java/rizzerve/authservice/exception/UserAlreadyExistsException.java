package rizzerve.authservice.exception;

public class UserAlreadyExistsException extends AuthServiceException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}