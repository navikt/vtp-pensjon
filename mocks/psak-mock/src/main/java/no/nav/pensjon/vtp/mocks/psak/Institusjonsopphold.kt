package no.nav.pensjon.vtp.mocks.psak

import java.util.*

data class Institusjonsopphold(
    var oppholdId: Int = 0,
    var tssEksternId: String? = null,
    var organisasjonsnummer: String? = null,
    var institusjonstype: String? = null,
    var varighet: String? = null,
    var kategori: String? = null,
    var startdato: Date? = null,
    var faktiskSluttdato: Date? = null,
    var forventetSluttdato: Date? = null,
    var kilde: String? = null,
    var overfoert: String? = null,
    var endretAv: String? = null,
    var endringstidspunkt: Date? = null,
)
