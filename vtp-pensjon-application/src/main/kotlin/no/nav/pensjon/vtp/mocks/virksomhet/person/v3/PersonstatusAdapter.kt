package no.nav.pensjon.vtp.mocks.virksomhet.person.v3

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonstatusModell
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatuser
import no.nav.tjeneste.virksomhet.person.v3.metadata.Endringstyper
import java.time.LocalDate
import java.time.LocalDate.of

const val ENDRET_AV = "vtp"

fun Endringstype.asEndringstyper(): Endringstyper = Endringstyper.fromValue(name)
fun Endringstype?.asEndringstyperOrNy() = this?.asEndringstyper() ?: Endringstyper.NY

fun statsborgerskap(allePersonstatus: List<PersonstatusModell>) = allePersonstatus
    .map {
        PersonstatusPeriode().apply {
            endretAv = ENDRET_AV

            endringstidspunkt = LocalDate.now().asXMLGregorianCalendar()

            endringstype = it.endringstype.asEndringstyperOrNy()

            personstatus = Personstatuser().apply {
                kodeverksRef = "Personstatuser"
                kodeRef = it.kode.name
                value = it.kode.name
            }

            periode = periode(
                fom = it.fom ?: of(2000, 1, 1),
                tom = it.tom ?: of(2050, 1, 1)
            )
        }
    }
