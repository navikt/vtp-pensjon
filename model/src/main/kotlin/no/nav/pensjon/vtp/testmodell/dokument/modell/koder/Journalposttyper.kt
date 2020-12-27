package no.nav.pensjon.vtp.testmodell.dokument.modell.koder

import java.util.*

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Journalposttyper/1?5
*/
enum class Journalposttyper(val code: String) {
    INNGAAENDE_DOKUMENT("I"),
    NOTAT("N"),
    UTGAAENDE_DOKUMENT("U");

    companion object {
        fun fromCode(code: String) = values().first { it.code == code }
    }
}
