package rizzerve.authservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyExistsExceptionTest {

    @Test
    void constructorShouldSetMessage() {
        String message = "Username already exists";

        UserAlreadyExistsException exception = new UserAlreadyExistsException(message);

        assertEquals(message, exception.getMessage());
    }
}
