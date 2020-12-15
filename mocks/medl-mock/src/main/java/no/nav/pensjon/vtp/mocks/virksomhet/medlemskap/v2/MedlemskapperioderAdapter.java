package no.nav.pensjon.vtp.mocks.virksomhet.medlemskap.v2;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import no.nav.pensjon.vtp.felles.ConversionUtils;
import no.nav.pensjon.vtp.testmodell.medlemskap.MedlemskapperiodeModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.Medlemsperiode;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.KildeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.LandkodeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.LovvalgMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.PeriodetypeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.StatuskodeMedTerm;
import no.nav.tjeneste.virksomhet.medlemskap.v2.informasjon.kodeverk.TrygdedekningMedTerm;

public class MedlemskapperioderAdapter {
    private final PersonModellRepository personModellRepository;

    public MedlemskapperioderAdapter(PersonModellRepository personModellRepository) {
        this.personModellRepository = personModellRepository;
    }

    public Optional<List<Medlemsperiode>> finnMedlemsperioder(String personIdent) {
        return ofNullable(personModellRepository.findById(personIdent))
                .map(pm -> {
                    List<Medlemsperiode> periodeListe = new ArrayList<>();
                    if(pm.getMedlemskap() != null) {
                        pm.getMedlemskap().getPerioder().forEach(medlemsskapsperiode -> periodeListe.add(tilMedlemsperiode(medlemsskapsperiode)));
                    }
                    return periodeListe;
                });
    }

    private Medlemsperiode tilMedlemsperiode(MedlemskapperiodeModell medlemsskapsperiode) {
        return new Medlemsperiode()
            .withId(medlemsskapsperiode.getId())
            .withFraOgMed(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getFom()))
            .withTilOgMed(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getTom()))
            .withDatoBesluttet(ConversionUtils.convertToXMLGregorianCalendar(medlemsskapsperiode.getBesluttetDato()))
            .withLand(new LandkodeMedTerm().withValue(medlemsskapsperiode.getLandkode().name()))

            .withTrygdedekning(new TrygdedekningMedTerm().withValue(medlemsskapsperiode.getDekningType().getKode()))
            .withType(new PeriodetypeMedTerm().withValue("PMMEDSKP")) // medlemsskapsperiode.getType().getKode()
            .withKilde(new KildeMedTerm().withValue(medlemsskapsperiode.getKilde().name()))
            .withStatus(new StatuskodeMedTerm().withValue(medlemsskapsperiode.getStatus().name()))
            .withLovvalg(new LovvalgMedTerm().withValue(medlemsskapsperiode.getLovvalgType().name()))
        ;
    }
}
