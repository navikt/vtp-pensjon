package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v5

import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell
import no.nav.tjeneste.virksomhet.organisasjon.v5.informasjon.*

fun mapOrganisasjonFraModell(modell: OrganisasjonModell): Organisasjon {
    return Virksomhet().apply {
        orgnummer = modell.orgnummer

        navn = UstrukturertNavn().apply {
            navnelinje.addAll(
                modell.navn?.navnelinje ?: emptyList()
            )
        }
        organisasjonDetaljer = modell.organisasjonDetaljer
            ?.let {
                OrganisasjonsDetaljer().apply {
                    registreringsDato = it.registreringsDato?.asXMLGregorianCalendar()
                }
            }
            ?: OrganisasjonsDetaljer()

        virksomhetDetaljer = VirksomhetDetaljer()
    }
}
