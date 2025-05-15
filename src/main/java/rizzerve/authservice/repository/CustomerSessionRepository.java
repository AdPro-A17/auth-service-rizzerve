package rizzerve.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rizzerve.authservice.model.CustomerSession;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerSessionRepository extends JpaRepository<CustomerSession, UUID> {
    Optional<CustomerSession> findBySessionToken(String sessionToken);
    Optional<CustomerSession> findByTableNumberAndActive(Integer tableNumber, boolean active);
}