package no.nav.pensjon.vtp.configuration.graphql.model

import java.util.*

data class Navn(
    val fornavn: String,
    val mellomnavn: String?,
    val etternavn: String,
    val forkortetNavn: String?,
    val gyldigFraOgMed: Date?,
    val metadata: Metadata
)