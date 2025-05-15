package rizzerve.authservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import rizzerve.authservice.model.Admin;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void findByUsernameShouldReturnAdmin() {
        String username = "testadmin";
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setName("Test Admin");
        admin.setPassword("encodedPassword");
        adminRepository.save(admin);

        Optional<Admin> foundAdmin = adminRepository.findByUsername(username);

        assertTrue(foundAdmin.isPresent());
        assertEquals(username, foundAdmin.get().getUsername());
    }

    @Test
    void findByUsernameShouldReturnEmptyOptionalWhenNotFound() {
        Optional<Admin> foundAdmin = adminRepository.findByUsername("nonexistent");

        assertFalse(foundAdmin.isPresent());
    }

    @Test
    void existsByUsernameShouldReturnTrueWhenExists() {
        String username = "existinguser";
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setName("Existing User");
        admin.setPassword("encodedPassword");
        adminRepository.save(admin);

        boolean exists = adminRepository.existsByUsername(username);

        assertTrue(exists);
    }

    @Test
    void existsByUsernameShouldReturnFalseWhenNotExists() {
        boolean exists = adminRepository.existsByUsername("nonexistent");

        assertFalse(exists);
    }
}
