package no.nav.pensjon.vtp.mocks.kontoregister;

import java.time.LocalDateTime;

data class Kontoinformasjon(
    val kontohaver: String,
    val kontonummer: String,
    val utenlandskKontoInfo: UtenlandskKontoInfo? = null,
    val gyldigFom: LocalDateTime,
    val gyldigTom:LocalDateTime?,
    val endretAv: String?,
    val opprettetAv: String,
    val kilde: String?,
)

data class UtenlandskKontoInfo(
    val banknavn: String?,
    val bankkode: String?,
    val bankLandkode: String,
    val valutakode: String,
    val swiftBicKode: String?,
    val bankadresse1: String?,
    val bankadresse2: String?,
    val bankadresse3: String?,
) {}
