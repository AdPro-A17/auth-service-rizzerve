package rizzerve.authservice.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void testNoArgsConstructor() {
        Admin admin = new Admin();
        assertNotNull(admin);
        assertNull(admin.getId());
        assertNull(admin.getName());
        assertNull(admin.getUsername());
        assertNull(admin.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Test Admin";
        String username = "testadmin";
        String password = "testpass";

        Admin admin = new Admin(id, name, username, password);

        assertEquals(id, admin.getId());
        assertEquals(name, admin.getName());
        assertEquals(username, admin.getUsername());
        assertEquals(password, admin.getPassword());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        String name = "Test Admin";
        String username = "testadmin";
        String password = "testpass";

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
    }

    @Test
    void testSettersAndGetters() {
        Admin admin = new Admin();
        UUID id = UUID.randomUUID();
        String name = "Test Admin";
        String username = "testadmin";
        String password = "testpass";

        admin.setId(id);
        admin.setName(name);
        admin.setUsername(username);
        admin.setPassword(password);

        assertEquals(id, admin.getId());
        assertEquals(name, admin.getName());
        assertEquals(username, admin.getUsername());
        assertEquals(password, admin.getPassword());
    }

    @Test
    void testGetAuthorities() {
        Admin admin = new Admin();
        Collection<? extends GrantedAuthority> authorities = admin.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testIsAccountNonExpired() {
        Admin admin = new Admin();
        assertTrue(admin.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        Admin admin = new Admin();
        assertTrue(admin.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        Admin admin = new Admin();
        assertTrue(admin.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        Admin admin = new Admin();
        assertTrue(admin.isEnabled());
    }

    @Test
    void testUserDetailsInterface() {
        String username = "testadmin";
        String password = "testpass";

        Admin admin = Admin.builder()
                .username(username)
                .password(password)
                .build();

        assertEquals(username, admin.getUsername());
        assertEquals(password, admin.getPassword());
        assertNotNull(admin.getAuthorities());
        assertTrue(admin.isAccountNonExpired());
        assertTrue(admin.isAccountNonLocked());
        assertTrue(admin.isCredentialsNonExpired());
        assertTrue(admin.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        String name = "Test Admin";
        String username = "testadmin";
        String password = "testpass";

        Admin admin1 = new Admin(id, name, username, password);
        Admin admin2 = new Admin(id, name, username, password);
        Admin admin3 = new Admin(UUID.randomUUID(), name, username, password);
        Admin admin4 = new Admin(id, "different", username, password);
        Admin admin5 = new Admin(id, name, "different", password);
        Admin admin6 = new Admin(id, name, username, "different");

        Admin admin7 = new Admin(null, name, username, password);
        Admin admin8 = new Admin(id, null, username, password);
        Admin admin9 = new Admin(id, name, null, password);
        Admin admin10 = new Admin(id, name, username, null);
        Admin admin11 = new Admin(null, null, null, null);
        Admin admin12 = new Admin(null, null, null, null);

        assertEquals(admin1, admin2);
        assertEquals(admin1.hashCode(), admin2.hashCode());

        assertNotEquals(admin1, admin3);
        assertNotEquals(admin1, admin4);
        assertNotEquals(admin1, admin5);
        assertNotEquals(admin1, admin6);
        assertNotEquals(admin1, admin7);
        assertNotEquals(admin1, admin8);
        assertNotEquals(admin1, admin9);
        assertNotEquals(admin1, admin10);

        assertEquals(admin11, admin12);
        assertEquals(admin11.hashCode(), admin12.hashCode());

        assertNotEquals(admin7, admin8);
        assertNotEquals(admin8, admin9);
        assertNotEquals(admin9, admin10);
        assertNotEquals(admin7, admin11);
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        String name = "Test Admin";
        String username = "testadmin";
        String password = "testpass";

        Admin admin = new Admin(id, name, username, password);
        String toString = admin.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Admin"));
        assertTrue(toString.contains(id.toString()));
        assertTrue(toString.contains(name));
        assertTrue(toString.contains(username));
        assertTrue(toString.contains(password));
    }

    @Test
    void testEqualsWithNull() {
        Admin admin = new Admin();
        assertNotEquals(null, admin);
    }

    @Test
    void testEqualsWithSameReference() {
        Admin admin = new Admin();
        Admin adminTest = new Admin();
        assertEquals(admin, adminTest);
    }

    @Test
    void canEqual() {
        Admin admin1 = new Admin();
        Admin admin2 = new Admin();
        assertTrue(admin1.canEqual(admin2));
        assertFalse(admin1.canEqual("string"));
    }

    @Test
    void testWithNullValues() {
        Admin admin = Admin.builder()
                .id(null)
                .name(null)
                .username(null)
                .password(null)
                .build();

        assertNull(admin.getId());
        assertNull(admin.getName());
        assertNull(admin.getUsername());
        assertNull(admin.getPassword());

        assertNotNull(admin.getAuthorities());
        assertTrue(admin.isAccountNonExpired());
        assertTrue(admin.isAccountNonLocked());
        assertTrue(admin.isCredentialsNonExpired());
        assertTrue(admin.isEnabled());
    }

    @Test
    void testAuthoritiesImmutability() {
        Admin admin = new Admin();
        Collection<? extends GrantedAuthority> authorities = admin.getAuthorities();

        assertEquals(1, authorities.size());
        GrantedAuthority authority = authorities.iterator().next();
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }
}