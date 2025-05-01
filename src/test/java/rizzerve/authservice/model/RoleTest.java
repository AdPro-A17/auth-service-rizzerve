package rizzerve.authservice.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {

    @Test
    void ensureRoleValues() {
        assertEquals(2, Role.values().length);
        assertEquals(Role.CUSTOMER, Role.valueOf("CUSTOMER"));
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
    }

    @Test
    void ensureRoleOrdinals() {
        assertEquals(0, Role.CUSTOMER.ordinal());
        assertEquals(1, Role.ADMIN.ordinal());
    }
}