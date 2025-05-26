package rizzerve.authservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class AdminAlreadyExistsExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        String message = "Admin already exists";
        AdminAlreadyExistsException exception = new AdminAlreadyExistsException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_ShouldHandleNullMessage() {
        AdminAlreadyExistsException exception = new AdminAlreadyExistsException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void constructor_ShouldHandleEmptyMessage() {
        String message = "";
        AdminAlreadyExistsException exception = new AdminAlreadyExistsException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void exception_ShouldExtendRuntimeException() {
        AdminAlreadyExistsException exception = new AdminAlreadyExistsException("test");

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void exception_ShouldHaveResponseStatusAnnotation() {
        Class<AdminAlreadyExistsException> clazz = AdminAlreadyExistsException.class;

        assertTrue(clazz.isAnnotationPresent(ResponseStatus.class));
    }

    @Test
    void responseStatusAnnotation_ShouldHaveConflictStatus() {
        Class<AdminAlreadyExistsException> clazz = AdminAlreadyExistsException.class;
        ResponseStatus annotation = clazz.getAnnotation(ResponseStatus.class);

        assertEquals(HttpStatus.CONFLICT, annotation.value());
    }

    @Test
    void exception_ShouldBeThrowable() {
        String message = "Test admin exists";

        assertThrows(AdminAlreadyExistsException.class, () -> {
            throw new AdminAlreadyExistsException(message);
        });
    }

    @Test
    void thrownException_ShouldContainCorrectMessage() {
        String message = "Admin with username 'test' already exists";

        AdminAlreadyExistsException exception = assertThrows(AdminAlreadyExistsException.class, () -> {
            throw new AdminAlreadyExistsException(message);
        });

        assertEquals(message, exception.getMessage());
    }
}