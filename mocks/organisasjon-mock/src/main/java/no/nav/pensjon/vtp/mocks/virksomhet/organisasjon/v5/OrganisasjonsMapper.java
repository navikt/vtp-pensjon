package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v5;

import static java.util.Optional.ofNullable;

import no.nav.pensjon.vtp.felles.ConversionUtils;
import no.nav.pensjon.vtp.testmodell.organisasjon.Navn;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonDetaljerModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.tjeneste.virksomhet.organisasjon.v5.informasjon.*;

import java.util.Collections;

public class OrganisasjonsMapper {

    public static Organisasjon mapOrganisasjonFraModell(OrganisasjonModell modell){
        Virksomhet organisasjon = new Virksomhet();
        organisasjon.setOrgnummer(modell.getOrgnummer());
        UstrukturertNavn ustrukturertNavn = new UstrukturertNavn();
        ustrukturertNavn.getNavnelinje().addAll(ofNullable(modell.getNavn()).map(Navn::getNavnelinje).orElseGet(Collections::emptyList));
        organisasjon.setNavn(ustrukturertNavn);
        organisasjon.setOrganisasjonDetaljer(mapOrganisasjonDetaljerFraModell(modell.getOrganisasjonDetaljer()));
        organisasjon.setVirksomhetDetaljer(new VirksomhetDetaljer());
        return organisasjon;
    }

    public static OrganisasjonsDetaljer mapOrganisasjonDetaljerFraModell(OrganisasjonDetaljerModell detaljer) {
        OrganisasjonsDetaljer organisasjonsDetaljer = new OrganisasjonsDetaljer();
        if(!(null == detaljer)){
            if(!(null == detaljer.getRegistreringsDato())) {
                organisasjonsDetaljer.setRegistreringsDato(ConversionUtils.convertToXMLGregorianCalendar(detaljer.getRegistreringsDato()));
            }
        }
        return organisasjonsDetaljer;
    }
}


