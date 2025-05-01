package rizzerve.authservice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import rizzerve.authservice.model.User;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}