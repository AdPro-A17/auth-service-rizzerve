package rizzerve.authservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfileRequestTest {

    @Test
    void gettersAndSettersShouldWorkCorrectly() {
        ProfileRequest request = new ProfileRequest();
        String name = "Test User";

        request.setName(name);

        assertEquals(name, request.getName());
    }
}