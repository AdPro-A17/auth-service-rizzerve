package rizzerve.authservice.client;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableAvailabilityResponseTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        TableAvailabilityResponse response = new TableAvailabilityResponse(true, "Available");
        assertThat(response.isAvailable()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Available");
    }

    @Test
    void testSetters() {
        TableAvailabilityResponse response = new TableAvailabilityResponse();
        response.setAvailable(false);
        response.setMessage("Not available");
        assertThat(response.isAvailable()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Not available");
    }
}