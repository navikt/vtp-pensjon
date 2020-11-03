package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering;

import static org.springframework.util.StringUtils.isEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("WeakerAccess")
public class IsoDateTimeValidator implements ConstraintValidator<IsoDateTime, String> {

    @Override
    public void initialize(IsoDateTime isoDateTime) {
        //JSR303
    }

    @Override
    public boolean isValid(String dateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (!isEmpty(dateTime)) {
            try {
                LocalDate.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

}
