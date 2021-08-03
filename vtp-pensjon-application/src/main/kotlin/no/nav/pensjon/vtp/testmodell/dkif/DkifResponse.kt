package no.nav.pensjon.vtp.testmodell.dkif

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DkifResponse(
    val kontaktinfo: Map<String, Kontaktinfo>?,
    val feil: Map<String, Feil>?
)
