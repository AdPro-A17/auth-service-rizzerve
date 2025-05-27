package rizzerve.authservice.dto.admin;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AdminUpdateNameRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validation_ShouldPass_WhenNameIsValid() {
        AdminUpdateNameRequest request = AdminUpdateNameRequest.builder()
                .name("Valid Name")
                .build();

        Set<ConstraintViolation<AdminUpdateNameRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "  \t\n  "})
    void validation_ShouldFail_WhenNameIsNullOrBlank(String invalidName) {
        AdminUpdateNameRequest request = AdminUpdateNameRequest.builder()
                .name(invalidName)
                .build();

        Set<ConstraintViolation<AdminUpdateNameRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Name is required");
    }

    @Test
    void builder_ShouldCreateValidObject() {
        String expectedName = "Test Name";

        AdminUpdateNameRequest request = AdminUpdateNameRequest.builder()
                .name(expectedName)
                .build();

        assertThat(request.getName()).isEqualTo(expectedName);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        AdminUpdateNameRequest request = new AdminUpdateNameRequest();
        String expectedName = "New Name";

        request.setName(expectedName);

        assertThat(request.getName()).isEqualTo(expectedName);
    }
}