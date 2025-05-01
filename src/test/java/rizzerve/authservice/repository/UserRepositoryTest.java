package rizzerve.authservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername() {
        User testUser = User.builder()
                .name("Test User")
                .username("testuser")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        entityManager.persist(testUser);
        entityManager.flush();

        Optional<User> found = userRepository.findByUsername("testuser");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
        assertEquals("Test User", found.get().getName());
    }

    @Test
    void findByUsernameNotFound() {
        Optional<User> found = userRepository.findByUsername("nonexistentuser");
        assertFalse(found.isPresent());
    }

    @Test
    void existsByUsername() {
        User testUser = User.builder()
                .name("Test User")
                .username("testuser")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        entityManager.persist(testUser);
        entityManager.flush();

        boolean exists = userRepository.existsByUsername("testuser");
        assertTrue(exists);
    }

    @Test
    void existsByUsernameNotFound() {
        boolean exists = userRepository.existsByUsername("nonexistentuser");
        assertFalse(exists);
    }

    @Test
    void saveUser() {
        User newUser = User.builder()
                .name("New User")
                .username("newuser")
                .password("encodedPassword")
                .role(Role.ADMIN)
                .build();

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId());
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("New User", savedUser.getName());
        assertEquals(Role.ADMIN, savedUser.getRole());
    }
}