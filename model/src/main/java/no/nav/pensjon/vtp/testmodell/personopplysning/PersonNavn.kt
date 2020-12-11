package no.nav.pensjon.vtp.testmodell.personopplysning

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn

data class PersonNavn(
    @JsonProperty("fornavn")
    val fornavn: String,

    @JsonProperty("etternavn")
    val etternavn: String,

    @JsonProperty("kjønn")
    val kjønn: Kjønn?
) {
    @JsonProperty
    fun getFulltnavn(): String = "$fornavn $etternavn"
}
