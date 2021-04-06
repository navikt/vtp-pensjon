package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class OneOrMoreOfValidator : ConstraintValidator<OneOrMoreOf, List<String?>?> {
    private lateinit var legalValues: Array<String>

    override fun initialize(oneOrMoreOf: OneOrMoreOf) {
        legalValues = oneOrMoreOf.legalValues
    }

    override fun isValid(list: List<String?>?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return if (list == null || list.isEmpty()) {
            true
        } else {
            val count = list
                .filter { entry: String? -> listOf(*legalValues).contains(entry) }
                .count()
            count == list.size
        }
    }
}
