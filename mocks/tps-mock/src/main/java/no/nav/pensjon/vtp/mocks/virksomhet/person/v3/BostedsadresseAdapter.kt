package no.nav.pensjon.vtp.mocks.virksomhet.person.v3

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bostedsadresse
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode
import java.time.LocalDate

fun fra(addresser: List<AdresseModell>) = addresser
    .map {
        BostedsadressePeriode().apply {
            endretAv = ENDRET_AV
            endringstidspunkt = (it.fom ?: LocalDate.now()).asXMLGregorianCalendar()
            endringstype = it.endringstype.asEndringstyperOrNy()

            bostedsadresse = Bostedsadresse().apply {
                endretAv = ENDRET_AV
                endringstidspunkt = (it.fom ?: LocalDate.now()).asXMLGregorianCalendar()
                endringstype = it.endringstype.asEndringstyperOrNy()
                strukturertAdresse = tilStrukturert(it)
            }

            periode = periode(
                fom = it.fom ?: LocalDate.of(2000, 1, 1),
                tom = it.tom ?: LocalDate.of(2050, 1, 1)
            )
        }
    }
