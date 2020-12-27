package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import org.springframework.util.ObjectUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class IsoDateTimeValidator : ConstraintValidator<IsoDateTime?, String?> {
    override fun initialize(isoDateTime: IsoDateTime?) {
        // JSR303
    }

    override fun isValid(dateTime: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (!ObjectUtils.isEmpty(dateTime)) {
            try {
                LocalDate.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (e: Exception) {
                return false
            }
        }
        return true
    }
}
