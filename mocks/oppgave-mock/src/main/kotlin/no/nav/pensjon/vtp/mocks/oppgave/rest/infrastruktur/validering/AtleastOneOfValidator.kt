package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class AtleastOneOfValidator : ConstraintValidator<AtleastOneOf, Any?> {
    private lateinit var fields: Array<String>

    override fun initialize(atleastOneOf: AtleastOneOf) {
        fields = atleastOneOf.fields
    }

    override fun isValid(o: Any?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return countFieldsMatching(o!!, fields) >= 1L
    }
}
