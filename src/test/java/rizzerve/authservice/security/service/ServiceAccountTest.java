package rizzerve.authservice.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceAccountTest {

    @Test
    void constructor_shouldSetServiceName() {
        String serviceName = "test-service";
        ServiceAccount serviceAccount = new ServiceAccount(serviceName);
        assertEquals(serviceName, serviceAccount.getServiceName());
        assertEquals(serviceName, serviceAccount.getUsername());
    }

    @Test
    void getAuthorities_shouldReturnAdminRole() {
        ServiceAccount serviceAccount = new ServiceAccount("test-service");
        Collection<? extends GrantedAuthority> authorities = serviceAccount.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void getPassword_shouldReturnNull() {
        ServiceAccount serviceAccount = new ServiceAccount("test-service");
        assertNull(serviceAccount.getPassword());
    }

    @Test
    void accountStatuses_shouldReturnTrue() {
        ServiceAccount serviceAccount = new ServiceAccount("test-service");
        assertTrue(serviceAccount.isAccountNonExpired());
        assertTrue(serviceAccount.isAccountNonLocked());
        assertTrue(serviceAccount.isCredentialsNonExpired());
        assertTrue(serviceAccount.isEnabled());
    }

    @Test
    void setServiceName_shouldUpdateServiceName() {
        ServiceAccount serviceAccount = new ServiceAccount("old-service");
        String newServiceName = "new-service";
        serviceAccount.setServiceName(newServiceName);
        assertEquals(newServiceName, serviceAccount.getServiceName());
        assertEquals(newServiceName, serviceAccount.getUsername());
    }

    @Test
    void defaultConstructor_shouldCreateEmptyAccount() {
        ServiceAccount serviceAccount = new ServiceAccount();
        assertNull(serviceAccount.getServiceName());
        assertNull(serviceAccount.getUsername());
    }
}
