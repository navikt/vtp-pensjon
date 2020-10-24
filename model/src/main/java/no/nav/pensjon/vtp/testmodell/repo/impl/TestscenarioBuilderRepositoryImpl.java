package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.pensjon.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.util.TestdataUtil;

@Component
public class TestscenarioBuilderRepositoryImpl implements TestscenarioBuilderRepository {

    private static final Logger log = LoggerFactory.getLogger(TestscenarioBuilderRepositoryImpl.class);

    private final Map<String, Testscenario> scenarios = new ConcurrentHashMap<>(); // not ordered for front-end

    private final PersonIndeks personIndeks;
    private final InntektYtelseIndeks inntektYtelseIndeks;
    private final OrganisasjonIndeks organisasjonIndeks;

    public TestscenarioBuilderRepositoryImpl(PersonIndeks personIndeks,
            InntektYtelseIndeks inntektYtelseIndeks, OrganisasjonIndeks organisasjonIndeks) {
        this.personIndeks = personIndeks;
        this.inntektYtelseIndeks = inntektYtelseIndeks;
        this.organisasjonIndeks = organisasjonIndeks;
    }

    @Override
    public Map<String, Testscenario> getTestscenarios() {
        return scenarios;
    }

    @Override
    public Testscenario getTestscenario(String id) {
        return scenarios.get(id);
    }

    public void indekser(Testscenario testScenario) {
        scenarios.put(testScenario.getId(), testScenario);
        Personopplysninger personopplysninger = testScenario.getPersonopplysninger();
        if (personopplysninger == null) {
            log.warn("TestscenarioImpl mangler innhold:" + testScenario);
        } else {
            SøkerModell søker = personopplysninger.getSøker();
            PersonNavn sokerNavn = TestdataUtil.getSokerName(søker);
            søker.setFornavn(sokerNavn.getFornavn());
            søker.setEtternavn(sokerNavn.getEtternavn());
            personIndeks.leggTil(søker);

            AnnenPartModell annenPart = personopplysninger.getAnnenPart();
            if(annenPart != null){
                PersonNavn annenPartNavn = TestdataUtil.getAnnenPartName(søker, annenPart);
                annenPart.setFornavn(annenPartNavn.getFornavn());
                annenPart.setEtternavn(annenPartNavn.getEtternavn());
                personIndeks.leggTil(annenPart);
            }
            personIndeks.indekserFamilierelasjonBrukere(personopplysninger.getFamilierelasjoner());

            personIndeks.indekserPersonopplysningerByIdent(personopplysninger);
            testScenario.getPersonligArbeidsgivere().forEach(personIndeks::leggTil);

            inntektYtelseIndeks.leggTil(personopplysninger.getSøker().getIdent(), testScenario.getSøkerInntektYtelse());
            if (personopplysninger.getAnnenPart() != null) {
                inntektYtelseIndeks.leggTil(personopplysninger.getAnnenPart().getIdent(), testScenario.getAnnenpartInntektYtelse());
            }
        }

        OrganisasjonModeller organisasjonModeller = testScenario.getOrganisasjonModeller();
        if (organisasjonModeller != null) {
            List<OrganisasjonModell> modeller = organisasjonModeller.getModeller();
            organisasjonIndeks.leggTil(modeller);
        }
    }

    @Override
    public Boolean slettScenario(String id) {
        return scenarios.remove(id) != null;
    }
}
