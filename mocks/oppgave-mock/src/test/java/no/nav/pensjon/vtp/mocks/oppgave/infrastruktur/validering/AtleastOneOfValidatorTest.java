package no.nav.pensjon.vtp.mocks.oppgave.infrastruktur.validering;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Test;
import org.springframework.context.annotation.Bean;

public class AtleastOneOfValidatorTest {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @SuppressWarnings({"FieldMayBeFinal"})
    @AtleastOneOf(fields = {"fieldA", "fieldB"})
    public static class TestClass {
        private String fieldA;
        private String fieldB;

        public TestClass(String fieldA, String fieldB) {
            this.fieldA = fieldA;
            this.fieldB = fieldB;
        }

        public String getFieldA() {
            return fieldA;
        }

        public String getFieldB() {
            return fieldB;
        }
    }

    @Test
    public void all_fields_null__gives_constraint_violation() {
        assertConstraintViolations(null, null);
    }

    @Test
    public void all_fields_blank__gives_constraint_violation() {
        assertConstraintViolations("", "");
    }

    @Test
    public void blank_and_null_field__gives_constraint_violation() {
        assertConstraintViolations("", null);
        assertConstraintViolations(null, "");
    }

    @Test
    public void one_field_set__is_valid() {
        assertValid("value", null);
        assertValid("value", "");
        assertValid(null, "value");
        assertValid("", "value");
    }

    @Test
    public void all_fields_set__is_valid() {
        assertValid("value", "value");
    }

    private void assertValid(String fieldA, String fieldB) {
        assertThat(validator.validate(new TestClass(fieldA, fieldB)))
                .isEmpty();
    }

    private void assertConstraintViolations(String fieldA, String fieldB) {
        assertThat(validator.validate(new TestClass(fieldA, fieldB)))
                .hasSize(1);
    }
}