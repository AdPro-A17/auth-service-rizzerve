package rizzerve.authservice.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTests {

    @Test
    void adminModelTest() {
        UUID id = UUID.randomUUID();
        String name = "Test Admin";
        String username = "testadmin";
        String password = "password123";

        Admin admin = Admin.builder()
                .id(id)
                .name(name)
                .username(username)
                .password(password)
                .build();

        assertEquals(id, admin.getId());
        assertEquals(name, admin.getName());
        assertEquals(username, admin.getUsername());
        assertEquals(password, admin.getPassword());

        Collection<? extends GrantedAuthority> authorities = admin.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        assertTrue(admin.isAccountNonExpired());
        assertTrue(admin.isAccountNonLocked());
        assertTrue(admin.isCredentialsNonExpired());
        assertTrue(admin.isEnabled());
    }
}
