package no.nav.pensjon.vtp.core.annotations

import org.springframework.stereotype.Component
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * Indicates that the annotated class is a Soap WebService that should be
 * component scanned and registered with the given *path*.
 */
@Target(ANNOTATION_CLASS, CLASS)
@Retention
@MustBeDocumented
@Component
annotation class SoapService(
    val path: Array<String>
)
