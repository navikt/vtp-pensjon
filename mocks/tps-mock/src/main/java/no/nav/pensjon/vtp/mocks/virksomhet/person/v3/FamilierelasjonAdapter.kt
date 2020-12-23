package no.nav.pensjon.vtp.mocks.virksomhet.person.v3

import no.nav.pensjon.vtp.testmodell.personopplysning.FamilierelasjonModell
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjon
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjoner

fun tilFamilerelasjon2(relasjoner: Collection<Pair<FamilierelasjonModell, PersonModell>>) =
    relasjoner.map { (relasjon, person) ->
        Familierelasjon().apply {
            isHarSammeBosted = true
            tilRolle = Familierelasjoner().apply {
                value = relasjon.getRolleKode()
            }

            tilPerson = mapTilPerson(person)
        }
    }
