package no.nav.pensjon.vtp.mocks.virksomhet.person.v3

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.tjeneste.virksomhet.person.v3.informasjon.*
import java.lang.UnsupportedOperationException
import java.time.LocalDate

fun setAdresser(bruker: Bruker, person: PersonModell) {

    // sett gjeldende til første adresse
    bruker.gjeldendePostadressetype = Postadressetyper().apply {
        value = person.getGjeldendeadresseType()
    }

    for (a in person.adresser) {
        when (a.adresseType) {
            AdresseType.BOSTEDSADRESSE -> {
                bruker.bostedsadresse = tilBostedsadresse(a)
            }
            AdresseType.MIDLERTIDIG_POSTADRESSE -> {
                bruker.midlertidigPostadresse = tilMidlertidigPostadresse(a)
            }
            AdresseType.POSTADRESSE -> bruker.postadresse = postadresse(a as UstrukturertAdresseModell)
            else -> println("Ukjent adressetype: $a")
        }
    }
}

private fun tilBostedsadresse(a: AdresseModell) = Bostedsadresse().apply {
    strukturertAdresse = tilStrukturert(a)
}

private fun tilMidlertidigPostadresse(a: AdresseModell) =
    if (Landkode.NOR == a.land) {
        MidlertidigPostadresseNorge().apply {
            endretAv = ENDRET_AV
            endringstidspunkt = a.fom?.asXMLGregorianCalendar()
            postleveringsPeriode = gyldighetsperiode(a.fom, a.tom)
            strukturertAdresse = tilStrukturert(a)
        }
    } else {
        MidlertidigPostadresseUtland().apply {
            postleveringsPeriode = gyldighetsperiode(a.fom, a.tom)
            endretAv = ENDRET_AV
            endringstidspunkt = a.fom?.asXMLGregorianCalendar()
            ustrukturertAdresse = tilUstrukturert(a)
        }
    }

fun tilStrukturert(adr: AdresseModell) = when (adr) {
    is GateadresseModell -> {
        fraGateadresse(adr)
    }
    is PostboksadresseModell -> {
        fraPostbokadresse(adr)
    }
    else -> {
        throw UnsupportedOperationException("Har ikke implementert støtte for å konvertere fra " + adr.javaClass)
    }
}

fun tilUstrukturert(adr: AdresseModell) = if (adr is UstrukturertAdresseModell) {
    fraUstrukturertAdresse(adr)
} else {
    throw UnsupportedOperationException("Har ikke implementert støtte for å konvertere fra " + adr.javaClass)
}

private fun fraUstrukturertAdresse(adr: UstrukturertAdresseModell) =
    UstrukturertAdresse().apply {
        landkode = landkoder(adr.land)
        adresselinje1 = adr.adresseLinje1
        adresselinje2 = adr.adresseLinje2
        adresselinje3 = adr.adresseLinje3
        adresselinje4 = adr.adresseLinje4
        postnr = adr.postNr
        poststed = adr.poststed
    }

fun fraPostbokadresse(adr: PostboksadresseModell) = PostboksadresseNorsk().apply {
    landkode = landkoder(adr.land)
    postboksanlegg = adr.postboksanlegg
    postboksnummer = adr.postboksnummer
    poststed = poststed(adr.poststed)
}

private fun fraGateadresse(gateaddresse: GateadresseModell) = Gateadresse().apply {
    landkode = landkoder(gateaddresse.land)
    gatenavn = gateaddresse.gatenavn
    gatenummer = gateaddresse.gatenummer
    husbokstav = gateaddresse.husbokstav
    husnummer = gateaddresse.husnummer
    poststed = poststed(gateaddresse.postnummer)
    kommunenummer = gateaddresse.kommunenummer
    tilleggsadresse = gateaddresse.tilleggsadresse
    tilleggsadresseType = gateaddresse.tilleggsadresseType
}

private fun postadresse(adr: UstrukturertAdresseModell) = Postadresse().apply {
    endretAv = ENDRET_AV
    endringstidspunkt = (adr.fom ?: LocalDate.of(2000, 1, 1)).asXMLGregorianCalendar()
    endringstype = adr.endringstype.asEndringstyperOrNy()
    ustrukturertAdresse = tilUstrukturert(adr)
}

private fun poststed(postnummer: String?) =
    Postnummer().apply {
        kodeverksRef = "Postnummer"
        kodeRef = postnummer
        value = postnummer
    }

fun gyldighetsperiode(fom: LocalDate?, tom: LocalDate?) = Gyldighetsperiode().apply {
    this.fom = fom?.asXMLGregorianCalendar()
    this.tom = tom?.asXMLGregorianCalendar()
}

fun landkoder(land: Landkode) = Landkoder().apply {
    kodeverksRef = "Landkoder"
    kodeRef = land.name
    value = land.name
}
