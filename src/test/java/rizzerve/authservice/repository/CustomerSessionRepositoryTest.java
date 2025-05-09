package rizzerve.authservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rizzerve.authservice.model.CustomerSession;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CustomerSessionRepositoryTest {

    @Autowired
    private CustomerSessionRepository customerSessionRepository;

    @Test
    void findBySessionTokenShouldReturnSession() {
        String sessionToken = "test-session-token";
        CustomerSession session = CustomerSession.builder()
                .tableNumber(1)
                .sessionToken(sessionToken)
                .startTime(LocalDateTime.now())
                .active(true)
                .build();
        customerSessionRepository.save(session);

        Optional<CustomerSession> foundSession = customerSessionRepository.findBySessionToken(sessionToken);

        assertTrue(foundSession.isPresent());
        assertEquals(sessionToken, foundSession.get().getSessionToken());
    }

    @Test
    void findByTableNumberAndActiveShouldReturnActiveSession() {
        Integer tableNumber = 2;
        CustomerSession session = CustomerSession.builder()
                .tableNumber(tableNumber)
                .sessionToken("table-2-token")
                .startTime(LocalDateTime.now())
                .active(true)
                .build();
        customerSessionRepository.save(session);

        Optional<CustomerSession> foundSession = customerSessionRepository.findByTableNumberAndActive(tableNumber, true);

        assertTrue(foundSession.isPresent());
        assertEquals(tableNumber, foundSession.get().getTableNumber());
        assertTrue(foundSession.get().isActive());
    }

    @Test
    void findByTableNumberAndActiveShouldNotReturnInactiveSession() {
        Integer tableNumber = 3;
        CustomerSession session = CustomerSession.builder()
                .tableNumber(tableNumber)
                .sessionToken("table-3-token")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .active(false)
                .build();
        customerSessionRepository.save(session);

        Optional<CustomerSession> foundSession = customerSessionRepository.findByTableNumberAndActive(tableNumber, true);

        assertFalse(foundSession.isPresent());
    }
}
