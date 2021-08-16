package no.nav.pensjon.vtp.configuration.graphql.model

import java.util.*

data class Foedsel(
    val foedselsaar: Int? = null,
    val foedselsdato: Date? = null,
    val foedeland: String? = null,
    val foedested: String? = null,
    val foedekommune: String? = null,
    val metadata: Metadata
)

data class Doedsfall(
    val doedsdato: Date? = null,
    val metadata: Metadata
)

data class Statsborgerskap(
    val land: String,
    val bekreftelsesdato: Date? = null,
    val gyldigFraOgMed: Date? = null,
    val gyldigTilOgMed: Date? = null,
    val metadata: Metadata
)

data class Navn(
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String,
    val forkortetNavn: String?,
    val gyldigFraOgMed: Date?,
    val metadata: Metadata
)

data class Metadata(
    val historisk: Boolean,
    val master: String
)
