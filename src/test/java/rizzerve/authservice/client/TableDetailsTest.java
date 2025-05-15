package rizzerve.authservice.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableDetailsTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        TableDetails details = new TableDetails("id1", 1, "TERSEDIA");
        assertThat(details.getId()).isEqualTo("id1");
        assertThat(details.getNomorMeja()).isEqualTo(1);
        assertThat(details.getStatus()).isEqualTo("TERSEDIA");
    }

    @Test
    void testSetters() {
        TableDetails details = new TableDetails();
        details.setId("id2");
        details.setNomorMeja(2);
        details.setStatus("TERPAKAI");
        assertThat(details.getId()).isEqualTo("id2");
        assertThat(details.getNomorMeja()).isEqualTo(2);
        assertThat(details.getStatus()).isEqualTo("TERPAKAI");
    }
}