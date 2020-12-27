package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Suppress("unused")
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [AtleastOneOfValidator::class])
annotation class AtleastOneOf(
    val message: String = "{no.nav.oppgave.AtleastOneOf}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val fields: Array<String>
)
