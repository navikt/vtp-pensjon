package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@SuppressWarnings("WeakerAccess")
public class AtleastOneOfValidator implements ConstraintValidator<AtleastOneOf, Object> {
    private String[] fields;

    @Override
    public void initialize(AtleastOneOf atleastOneOf) {
        this.fields = atleastOneOf.fields();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return CountFieldsMatching.count(o, fields) >= 1L;
    }
}
