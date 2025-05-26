package rizzerve.authservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rizzerve.authservice.model.Admin;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void testFindByUsername() {
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        entityManager.persistAndFlush(admin);

        Optional<Admin> result = adminRepository.findByUsername("testadmin");

        assertTrue(result.isPresent());
        assertEquals("testadmin", result.get().getUsername());
        assertEquals("Test Admin", result.get().getName());
    }

    @Test
    void testFindByUsernameNotFound() {
        Optional<Admin> result = adminRepository.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void testExistsByUsername() {
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        entityManager.persistAndFlush(admin);

        assertTrue(adminRepository.existsByUsername("testadmin"));
        assertFalse(adminRepository.existsByUsername("nonexistent"));
    }

    @Test
    void testSaveAdmin() {
        Admin admin = Admin.builder()
                .name("New Admin")
                .username("newadmin")
                .password("password123")
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        assertNotNull(savedAdmin.getId());
        assertEquals("New Admin", savedAdmin.getName());
        assertEquals("newadmin", savedAdmin.getUsername());
    }

    @Test
    void testFindById() {
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        Admin savedAdmin = entityManager.persistAndFlush(admin);

        Optional<Admin> result = adminRepository.findById(savedAdmin.getId());

        assertTrue(result.isPresent());
        assertEquals(savedAdmin.getId(), result.get().getId());
    }

    @Test
    void testDeleteAdmin() {
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        Admin savedAdmin = entityManager.persistAndFlush(admin);

        adminRepository.deleteById(savedAdmin.getId());

        Optional<Admin> result = adminRepository.findById(savedAdmin.getId());
        assertFalse(result.isPresent());
    }
}