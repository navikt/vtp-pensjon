package no.nav.pensjon.vtp.mocks.virksomhet.person.v3

import no.nav.pensjon.vtp.testmodell.personopplysning.StatsborgerskapModell
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Periode
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Statsborgerskap
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper
import java.time.LocalDate

fun statsborgerskap(data: List<StatsborgerskapModell>) = data
    .map {
        StatsborgerskapPeriode().apply {
            endretAv = ENDRET_AV
            endringstidspunkt = (it.fom ?: LocalDate.now()).asXMLGregorianCalendar()

            endringstype = it.endringstype.asEndringstyperOrNy()

            periode = periode(
                fom = it.fom ?: LocalDate.of(2000, 1, 1),
                tom = it.tom ?: LocalDate.of(2050, 1, 1)
            )

            statsborgerskap = statsborgerskap(it)
        }
    }

fun statsborgerskap(st: StatsborgerskapModell?) = Statsborgerskap().apply {
    endretAv = ENDRET_AV

    endringstidspunkt = st?.fom?.asXMLGregorianCalendar()
    endringstype = Endringstyper.NY

    land = landkoder(st?.land)
}

fun periode(fom: LocalDate, tom: LocalDate) = Periode().apply {
    this.fom = fom.asXMLGregorianCalendar()
    this.tom = tom.asXMLGregorianCalendar()
}
