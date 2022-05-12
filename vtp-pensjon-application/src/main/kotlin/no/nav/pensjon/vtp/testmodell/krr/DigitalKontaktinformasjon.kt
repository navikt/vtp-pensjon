package no.nav.pensjon.vtp.testmodell.krr

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
data class DigitalKontaktinformasjon(
    @Id
    val personident: String,
    val aktiv: Boolean,
    val kanVarsles: Boolean? = null,
    val reservert: Boolean? = null,
    val spraak: String? = null,
    val epostadresse: String? = null,
    val epostadresseOppdatert: String? = null,
    val mobiltelefonnummer: String? = null,
    val mobiltelefonnummerOppdatert: String? = null,
    val sikkerDigitalPostkasse: SikkerDigitalPostkasse? = null
) {
    data class SikkerDigitalPostkasse(
        val adresse: String,
        val leverandoerAdresse: String?,
        val leverandoerSertifikat: String?
    )
}
