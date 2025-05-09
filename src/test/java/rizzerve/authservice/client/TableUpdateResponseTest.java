package rizzerve.authservice.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableUpdateResponseTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        TableDetails details = new TableDetails("id1", 1, "TERSEDIA");
        TableUpdateResponse response = new TableUpdateResponse("TERSEDIA", details);
        assertThat(response.getStatus()).isEqualTo("TERSEDIA");
        assertThat(response.getTable()).isEqualTo(details);
    }

    @Test
    void testSetters() {
        TableUpdateResponse response = new TableUpdateResponse();
        TableDetails details = new TableDetails("id2", 2, "TERPAKAI");
        response.setStatus("TERPAKAI");
        response.setTable(details);
        assertThat(response.getStatus()).isEqualTo("TERPAKAI");
        assertThat(response.getTable()).isEqualTo(details);
    }
}