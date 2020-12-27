package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class OneOfValidator : ConstraintValidator<OneOf, String> {
    private lateinit var legalValues: Array<String>

    override fun initialize(oneOf: OneOf) {
        legalValues = oneOf.legalValues
    }

    override fun isValid(oneOf: String, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return if (isBlank(oneOf)) {
            true
        } else legalValues.any { legalEntry: String -> legalEntry == oneOf }
    }

    companion object {
        fun isBlank(string: String): Boolean {
            return string.chars().allMatch { codePoint: Int -> Character.isWhitespace(codePoint) }
        }
    }
}
