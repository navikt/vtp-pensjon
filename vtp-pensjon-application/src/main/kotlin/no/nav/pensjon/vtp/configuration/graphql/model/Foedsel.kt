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