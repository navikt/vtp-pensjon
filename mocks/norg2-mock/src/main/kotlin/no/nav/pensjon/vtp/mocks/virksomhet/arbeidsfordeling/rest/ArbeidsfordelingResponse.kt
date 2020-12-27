package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class ArbeidsfordelingResponse(
    @JsonProperty("enhetNr")
    val enhetNr: String?,
    @JsonProperty("navn")
    val enhetNavn: String?,
    @JsonProperty("status")
    val enhetType: String?,
    @JsonProperty("type")
    val status: String?
)
