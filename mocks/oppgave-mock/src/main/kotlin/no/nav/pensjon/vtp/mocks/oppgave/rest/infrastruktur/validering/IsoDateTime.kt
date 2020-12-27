package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Suppress("unused")
@Target(AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [IsoDateTimeValidator::class])
annotation class IsoDateTime(
    val message: String = "{no.nav.oppgave.IsoDateTime.ugyldig}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
