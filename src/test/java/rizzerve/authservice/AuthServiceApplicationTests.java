package rizzerve.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthServiceApplicationTests {

    @Test
    void contextLoads(ApplicationContext context) {
        assertNotNull(context, "Application context should load");
    }
}