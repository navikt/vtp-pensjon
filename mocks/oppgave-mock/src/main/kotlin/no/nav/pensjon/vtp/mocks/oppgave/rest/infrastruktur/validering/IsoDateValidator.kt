package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import org.springframework.util.ObjectUtils.isEmpty
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class IsoDateValidator : ConstraintValidator<IsoDate?, String?> {
    override fun initialize(isoDate: IsoDate?) {
        // JSR303
    }

    override fun isValid(date: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        return when {
            isEmpty(date) -> true
            else ->
                try {
                    parse(date, ISO_LOCAL_DATE)
                    true
                } catch (e: Exception) {
                    false
                }
        }
    }
}
