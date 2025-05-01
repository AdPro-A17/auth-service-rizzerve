package rizzerve.authservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import rizzerve.authservice.model.Role;
import rizzerve.authservice.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_ExistingUser_ReturnsUser() {
        User user = User.builder()
                .name("Test User")
                .username("testuser")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        assertEquals(user.getPassword(), foundUser.get().getPassword());
        assertEquals(user.getRole(), foundUser.get().getRole());
    }

    @Test
    void findByUsername_NonExistentUser_ReturnsEmpty() {
        Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void existsByUsername_ExistingUser_ReturnsTrue() {
        User user = User.builder()
                .name("Test User")
                .username("testuser")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("testuser");
        assertTrue(exists);
    }

    @Test
    void existsByUsername_NonExistentUser_ReturnsFalse() {
        boolean exists = userRepository.existsByUsername("nonexistentuser");
        assertFalse(exists);
    }

    @Test
    void save_UserWithUniqueUsername_SavesSuccessfully() {
        User user = User.builder()
                .name("Test User")
                .username("uniqueuser")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getRole(), savedUser.getRole());
    }

    @Test
    void save_UserWithDuplicateUsername_ThrowsException() {
        User user1 = User.builder()
                .name("Test User 1")
                .username("sameusername")
                .password("password1")
                .role(Role.CUSTOMER)
                .build();

        User user2 = User.builder()
                .name("Test User 2")
                .username("sameusername")
                .password("password2")
                .role(Role.ADMIN)
                .build();

        userRepository.save(user1);

        assertThrows(Exception.class, () -> {
            userRepository.save(user2);
            userRepository.flush();
        });
    }
}