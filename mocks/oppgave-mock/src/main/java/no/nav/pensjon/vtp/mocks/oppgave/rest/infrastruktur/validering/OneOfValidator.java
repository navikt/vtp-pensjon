package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class OneOfValidator implements ConstraintValidator<OneOf, String> {
    private String[] legalValues;

    @Override
    public void initialize(OneOf oneOf) {
        this.legalValues = oneOf.legalValues();
    }

    @Override
    public boolean isValid(String oneOf, ConstraintValidatorContext constraintValidatorContext) {
        if (isBlank(oneOf)) {
            return true;
        }

        return Arrays.stream(legalValues).anyMatch(legalEntry -> legalEntry.equals(oneOf));
    }

    public static boolean isBlank(final String string) {
        return string.chars().allMatch(Character::isWhitespace);
    }
}
