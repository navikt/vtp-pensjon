package no.nav.pensjon.vtp.testmodell.brev

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class BrevMetadata {
    val brevkodeIBrevsystem: String? = null
    val redigerbart: Boolean = false
    val dekode: String? = null
    val brevkategori: String? = null
    val dokType: String? = null
    val sprak: List<String> = emptyList()
    val visIPselv: Boolean = false
    val utland:String? = null
    val brevregeltype: String? = null
    val brevkravtype: String? = null
    val brevkontekst: String? = null
    val dokumentkategori: String? = null
    val synligForVeileder: Boolean = false
    val prioritet: Int = 0
    val brevsystem: String? = null
    val brevgruppe: String? = null
    val dokumentmalId: String? = null
}