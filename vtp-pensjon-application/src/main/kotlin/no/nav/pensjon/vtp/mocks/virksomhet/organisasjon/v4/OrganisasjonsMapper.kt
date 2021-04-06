package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v4

import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonDetaljerModell
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.*

fun mapOrganisasjonFraModell(modell: OrganisasjonModell) = Virksomhet().apply {
    orgnummer = modell.orgnummer
    navn = UstrukturertNavn().apply {
        navnelinje += modell.navn?.navnelinje ?: emptyList()
    }
    organisasjonDetaljer = modell.organisasjonDetaljer
        ?.let { mapOrganisasjonDetaljerFraModell(it) }
        ?: OrganisasjonsDetaljer()
    virksomhetDetaljer = VirksomhetDetaljer()
}

fun mapOrganisasjonDetaljerFraModell(detaljer: OrganisasjonDetaljerModell) =
    OrganisasjonsDetaljer().apply {
        registreringsDato = detaljer.registreringsDato?.asXMLGregorianCalendar()
        datoSistEndret = detaljer.datoSistEndret?.asXMLGregorianCalendar()
    }
