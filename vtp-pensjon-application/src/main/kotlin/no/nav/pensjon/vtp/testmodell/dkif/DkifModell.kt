package no.nav.pensjon.vtp.testmodell.dkif

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks
import kotlin.collections.ArrayList

data class DkifModell(
    val personident: String,
    val kanVarsles: Boolean,
    val reservert: Boolean,
    val epostadresse: String?,
    val mobiltelefonnummer: String?,
    val spraak: String?
)

data class DkifModeller(
    @JsonProperty("kontaktinfo")
    val kontaktinfo: List<DkifModell>
)

fun dkifModellMapper(dkifModellList: List<DkifModell>, lokalIdentIndeks: LokalIdentIndeks): List<Kontaktinfo> {
    val kontaktinfoList = ArrayList<Kontaktinfo>()
    dkifModellList.forEach {
        kontaktinfoList.add(
            Kontaktinfo(
                personident = lokalIdentIndeks.getIdent(it.personident)!!,
                kanVarsles = it.kanVarsles,
                reservert = it.reservert,
                epostadresse = it.epostadresse,
                mobiltelefonnummer = it.mobiltelefonnummer,
                spraak = it.spraak
            )
        )
    }
    return kontaktinfoList
}
