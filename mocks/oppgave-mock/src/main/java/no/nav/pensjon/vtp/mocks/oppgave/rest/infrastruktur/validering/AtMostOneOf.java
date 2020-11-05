package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AtMostOneOfValidator.class)
public @interface AtMostOneOf {
    String message() default "{no.nav.oppgave.AtMostOneOf}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields();

}