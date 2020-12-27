package no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun

import com.fasterxml.jackson.annotation.JsonProperty

data class Oppføring(
    @JsonProperty("tekniskNavn")
    val tekniskNavn: String? = null,

    @JsonProperty("beløp")
    val verdi: String? = null
) {
    override fun toString(): String {
        return "{\n\"tekniskNavn\": \"$tekniskNavn\",\n\"verdi\": \"$verdi\"\n}"
    }
}
