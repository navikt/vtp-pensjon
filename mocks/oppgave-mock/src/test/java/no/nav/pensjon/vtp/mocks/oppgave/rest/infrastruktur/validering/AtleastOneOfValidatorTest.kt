package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import org.assertj.core.api.Assertions
import org.junit.Test
import javax.validation.Validation

class AtleastOneOfValidatorTest {
    @Suppress("unused")
    @AtleastOneOf(fields = ["fieldA", "fieldB"])
    class TestClass(val fieldA: String?, val fieldB: String?)

    @Test
    fun all_fields_null__gives_constraint_violation() {
        assertConstraintViolations(null, null)
    }

    @Test
    fun all_fields_blank__gives_constraint_violation() {
        assertConstraintViolations("", "")
    }

    @Test
    fun blank_and_null_field__gives_constraint_violation() {
        assertConstraintViolations("", null)
        assertConstraintViolations(null, "")
    }

    @Test
    fun one_field_set__is_valid() {
        assertValid("value", null)
        assertValid("value", "")
        assertValid(null, "value")
        assertValid("", "value")
    }

    @Test
    fun all_fields_set__is_valid() {
        assertValid("value", "value")
    }

    private fun assertValid(fieldA: String?, fieldB: String?) {
        Assertions.assertThat(validator.validate(TestClass(fieldA, fieldB)))
            .isEmpty()
    }

    private fun assertConstraintViolations(fieldA: String?, fieldB: String?) {
        Assertions.assertThat(validator.validate(TestClass(fieldA, fieldB)))
            .hasSize(1)
    }

    companion object {
        private val validator = Validation.buildDefaultValidatorFactory().validator
    }
}
