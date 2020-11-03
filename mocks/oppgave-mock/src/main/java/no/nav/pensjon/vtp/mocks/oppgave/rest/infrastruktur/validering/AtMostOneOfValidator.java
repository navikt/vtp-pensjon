package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering.CountFieldsMatching.count;

@SuppressWarnings("WeakerAccess")
public class AtMostOneOfValidator implements ConstraintValidator<AtMostOneOf, Object> {
    private String[] fields;

    @Override
    public void initialize(AtMostOneOf atMostOneOf) {
        this.fields = atMostOneOf.fields();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return count(o, fields) <= 1L;
    }
}
