package no.nav.pensjon.vtp.core.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Indicates that the annotated class is a JAX-RS Resource that should be
 * component scanned and registered om JAX-RS Application.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface JaxrsResource {
}
