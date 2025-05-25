package rizzerve.authservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import rizzerve.authservice.model.Admin;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void testFindByUsername() {
        // Given
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        entityManager.persistAndFlush(admin);

        // When
        Optional<Admin> result = adminRepository.findByUsername("testadmin");

        // Then
        assertTrue(result.isPresent());
        assertEquals("testadmin", result.get().getUsername());
        assertEquals("Test Admin", result.get().getName());
    }

    @Test
    void testFindByUsernameNotFound() {
        // When
        Optional<Admin> result = adminRepository.findByUsername("nonexistent");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testExistsByUsername() {
        // Given
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        entityManager.persistAndFlush(admin);

        // When & Then
        assertTrue(adminRepository.existsByUsername("testadmin"));
        assertFalse(adminRepository.existsByUsername("nonexistent"));
    }

    @Test
    void testSaveAdmin() {
        // Given
        Admin admin = Admin.builder()
                .name("New Admin")
                .username("newadmin")
                .password("password123")
                .build();

        // When
        Admin savedAdmin = adminRepository.save(admin);

        // Then
        assertNotNull(savedAdmin.getId());
        assertEquals("New Admin", savedAdmin.getName());
        assertEquals("newadmin", savedAdmin.getUsername());
    }

    @Test
    void testFindById() {
        // Given
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        Admin savedAdmin = entityManager.persistAndFlush(admin);

        // When
        Optional<Admin> result = adminRepository.findById(savedAdmin.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(savedAdmin.getId(), result.get().getId());
    }

    @Test
    void testDeleteAdmin() {
        // Given
        Admin admin = Admin.builder()
                .name("Test Admin")
                .username("testadmin")
                .password("password123")
                .build();
        Admin savedAdmin = entityManager.persistAndFlush(admin);

        // When
        adminRepository.deleteById(savedAdmin.getId());

        // Then
        Optional<Admin> result = adminRepository.findById(savedAdmin.getId());
        assertFalse(result.isPresent());
    }
}