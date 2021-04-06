package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import org.springframework.util.ObjectUtils
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class OrganisasjonsnummerValidator : ConstraintValidator<Organisasjonsnummer?, String> {
    override fun initialize(orgnr: Organisasjonsnummer?) {
        // JSR303
    }

    override fun isValid(orgnr: String, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        val orgnrVekter = intArrayOf(3, 2, 7, 6, 5, 4, 3, 2)

        return if (ObjectUtils.isEmpty(orgnr)) {
            true
        } else if (!orgnr.matches("\\A[89]\\d{8}\\Z".toRegex())) {
            false
        } else {
            var sum = 0
            for (i in 0..7) {
                val verdi = orgnr.substring(i, i + 1).toInt()
                sum += orgnrVekter[i] * verdi
            }
            val rest = sum % 11
            val kontrollSiffer = if (rest == 0) 0 else 11 - rest
            kontrollSiffer == orgnr.substring(8, 9).toInt()
        }
    }
}
