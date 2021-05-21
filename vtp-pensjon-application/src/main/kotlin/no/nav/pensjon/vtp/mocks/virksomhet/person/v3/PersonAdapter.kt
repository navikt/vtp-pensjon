package no.nav.pensjon.vtp.mocks.virksomhet.person.v3

import no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder
import no.nav.pensjon.vtp.testmodell.kodeverk.GeografiskTilknytningType
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.tjeneste.virksomhet.person.v3.informasjon.*
import java.util.Locale.getDefault

fun fra(person: PersonModell) = mapFraBruker(person).apply {
    kjoenn = Kjoenn().apply {
        kjoenn = Kjoennstyper().apply {
            value = person.getKjønnKode()
        }
    }

    foedselsdato = Foedselsdato().apply {
        foedselsdato = person.fødselsdato?.asXMLGregorianCalendar()
    }

    personnavn = Personnavn().apply {
        etternavn = person.etternavn.uppercase(getDefault())
        fornavn = person.fornavn.uppercase(getDefault())
        sammensattNavn = person.etternavn.uppercase(getDefault()) + " " + person.fornavn.uppercase(getDefault())
    }

    personstatus = Personstatus().apply {
        personstatus = Personstatuser().apply {
            value = person.getPersonstatusFoo().kode.name
        }
    }

    maalform = Spraak().apply {
        value = person.getSpråk2Bokstaver()
    }

    geografiskTilknytning = tilGeografiskTilknytning(person)

    sivilstand = Sivilstand().apply {
        sivilstand = Sivilstander().apply {
            value = person.getSivilstandFoo().kode.name
        }
    }

    diskresjonskode = person.diskresjonskode?.let { tilDiskresjonskode(it) }

    statsborgerskap = statsborgerskap(person.getStatsborgerskapFoo())
}.also {
    setAdresser(it, person)
}

fun mapTilPerson(modell: PersonModell) = Person().apply {
    aktoer = PersonIdent().apply {
        ident = NorskIdent().apply {
            ident = modell.ident
        }
    }
}

private fun mapFraBruker(person: PersonModell) = Bruker().apply {
    aktoer = PersonIdent().apply {
        ident = NorskIdent().apply {
            ident = person.ident

            type = Personidenter().apply {
                value = "fnr"
            }
        }
    }
}

fun tilDiskresjonskode(diskresjonskoder: Diskresjonskoder) =
    Diskresjonskoder().apply {
        withKodeverksRef("Diskresjonskoder")
        withKodeRef(diskresjonskoder.name)
        withValue(diskresjonskoder.name)
    }

fun tilGeografiskTilknytning(bruker: PersonModell) = bruker.geografiskTilknytning
    ?.let {
        when (it.type) {
            GeografiskTilknytningType.Land -> Land()
            GeografiskTilknytningType.Kommune -> Kommune()
            GeografiskTilknytningType.Bydel -> Bydel()
            else -> return null
        }.apply {
            geografiskTilknytning = it.kode
        }
    }
