package no.nav.pensjon.vtp.testmodell.dokument.modell.koder

import java.util.*

/*
    Hentet fra: https://modapp.adeo.no/kodeverksklient/viskodeverk/Journalstatuser/1?11
 */
enum class Journalstatus(val code: String) {
    JOURNALFØRT("J"),
    MOTTATT("MO"),
    MIDLERTIDIG_JOURNALFØRT("M"),
    AVBRUTT("A");

    companion object {
        fun fromCode(code: String) = values().first { it.code == code }
    }
}
