package no.nav.pensjon.vtp.testmodell.dokument.modell.koder

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Dokumentkategorier/1?7
 */
enum class Dokumentkategori(val code: String) {
    BREV("B"),
    ELEKTRONISK_DIALOG("ELEKTRONISK_DIALOG"),
    ELEKTRONISK_SKJEMA("ES"),
    FORVALTNINGSNOTAT("FORVALTNINGSNOTAT"),
    INFORMASJONSBREV("IB"),
    IKKE_TOLKBART_SKJEMA("IS"),
    KLAGE_ANKE("KA"),
    KONVERTERT_FRA_ELEKTRONSIK_ARKIV("KD"),
    KONVERTERT_DATA_FRA_SYSTEM("KS"),
    PUBLIKUMSBLANKETT_EOS("PUBL_BLANKETT_EOS"),
    STRUKTURERT_ELEKTRONISK_DOKUMENT_EU_EOS("SED"),
    SOKNAD("SOK"),
    TOLKBART_SKJEMA("TS"),
    VEDTAKSBREV("VB");

    companion object {
        fun fromCode(code: String) = values().first { it.code == code }
    }
}
