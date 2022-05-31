package no.nav.pensjon.vtp.testmodell.personopplysning

import java.time.LocalDateTime

data class InnflyttingTilNorgeModell(
    val fraflyttingsland: String,
    val folkeregistermetadata: FolkeregistermetadataModell
) {
    data class FolkeregistermetadataModell(
        val ajourholdstidspunkt: LocalDateTime?,
        val gyldighetstidspunkt: LocalDateTime?
    )
}
