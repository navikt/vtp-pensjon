package no.nav.pensjon.vtp.mocks.virksomhet.medlemskap.v2

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.medlemskap.MedlemskapperiodeModell
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.Medlemsperiode
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.*

fun tilMedlemsperiode(medlemsskapsperiode: MedlemskapperiodeModell): Medlemsperiode {
    return Medlemsperiode()
        .withId(medlemsskapsperiode.id)
        .withFraOgMed(medlemsskapsperiode.fom?.asXMLGregorianCalendar())
        .withTilOgMed(medlemsskapsperiode.tom?.asXMLGregorianCalendar())
        .withDatoBesluttet(medlemsskapsperiode.besluttetDato?.asXMLGregorianCalendar())
        .withLand(LandkodeMedTerm().withValue(medlemsskapsperiode.landkode.name))
        .withTrygdedekning(TrygdedekningMedTerm().withValue(medlemsskapsperiode.dekningType!!.kode))
        .withType(PeriodetypeMedTerm().withValue("PMMEDSKP")) // medlemsskapsperiode.getType().getKode()
        .withKilde(KildeMedTerm().withValue(medlemsskapsperiode.kilde.name))
        .withStatus(StatuskodeMedTerm().withValue(medlemsskapsperiode.status.name))
        .withLovvalg(LovvalgMedTerm().withValue(medlemsskapsperiode.lovvalgType!!.name))
}
