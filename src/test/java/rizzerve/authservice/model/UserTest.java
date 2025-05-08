package rizzerve.authservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .username("testuser")
                .password("password123")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    void getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }

    @Test
    void isAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void equalsAndHashCode() {
        User sameUser = User.builder()
                .id(user.getId())
                .name("Test User")
                .username("testuser")
                .password("password123")
                .role(Role.CUSTOMER)
                .build();

        User differentUser = User.builder()
                .id(UUID.randomUUID())
                .name("Different User")
                .username("differentuser")
                .password("password456")
                .role(Role.ADMIN)
                .build();

        assertEquals(user, sameUser);
        assertNotEquals(user, differentUser);
        assertEquals(user.hashCode(), sameUser.hashCode());
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }
}