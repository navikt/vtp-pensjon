package no.nav.pensjon.vtp.testmodell.dkif

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Kontaktinfo(
    @Id
    val personident: String,
    val kanVarsles: Boolean,
    val reservert: Boolean,
    val epostadresse: String?,
    val mobiltelefonnummer: String?,
    val spraak: String?
)
