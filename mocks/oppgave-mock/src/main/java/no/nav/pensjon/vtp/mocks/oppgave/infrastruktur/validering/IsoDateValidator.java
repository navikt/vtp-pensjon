package no.nav.pensjon.vtp.mocks.oppgave.infrastruktur.validering;

import static org.springframework.util.StringUtils.isEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("WeakerAccess")
public class IsoDateValidator implements ConstraintValidator<IsoDate, String> {

    @Override
    public void initialize(IsoDate isoDate) {
        //JSR303
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        if (!isEmpty(date)) {
            try {
                LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

}
