package rizzerve.authservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminNotFoundExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        String message = "Admin not found";
        AdminNotFoundException exception = new AdminNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_ShouldHandleNullMessage() {
        AdminNotFoundException exception = new AdminNotFoundException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void constructor_ShouldHandleEmptyMessage() {
        String message = "";
        AdminNotFoundException exception = new AdminNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void exception_ShouldExtendRuntimeException() {
        AdminNotFoundException exception = new AdminNotFoundException("test");

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void exception_ShouldHaveResponseStatusAnnotation() {
        Class<AdminNotFoundException> clazz = AdminNotFoundException.class;

        assertTrue(clazz.isAnnotationPresent(ResponseStatus.class));
    }

    @Test
    void responseStatusAnnotation_ShouldHaveNotFoundStatus() {
        Class<AdminNotFoundException> clazz = AdminNotFoundException.class;
        ResponseStatus annotation = clazz.getAnnotation(ResponseStatus.class);

        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }

    @Test
    void exception_ShouldBeThrowable() {
        String message = "Test admin not found";

        assertThrows(AdminNotFoundException.class, () -> {
            throw new AdminNotFoundException(message);
        });
    }

    @Test
    void thrownException_ShouldContainCorrectMessage() {
        String message = "Admin with id '123' not found";

        AdminNotFoundException exception = assertThrows(AdminNotFoundException.class, () -> {
            throw new AdminNotFoundException(message);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    void exception_ShouldHaveCause() {
        String message = "Admin not found";
        RuntimeException cause = new RuntimeException("Database error");
        AdminNotFoundException exception = new AdminNotFoundException(message);
        exception.initCause(cause);

        assertEquals(cause, exception.getCause());
    }
}