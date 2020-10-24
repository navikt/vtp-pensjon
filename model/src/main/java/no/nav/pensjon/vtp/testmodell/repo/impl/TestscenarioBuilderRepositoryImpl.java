package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import no.nav.pensjon.vtp.testmodell.util.TestdataUtil;
import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModeller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.testmodell.repo.BasisdataProvider;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.pensjon.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;

public abstract class TestscenarioBuilderRepositoryImpl implements TestscenarioBuilderRepository {

    private static final Logger log = LoggerFactory.getLogger(TestscenarioBuilderRepositoryImpl.class);

    private final BasisdataProvider basisdata;
    private final Map<String, Testscenario> scenarios = new ConcurrentHashMap<>(); // not ordered for front-end
    private final Map<String, LokalIdentIndeks> identer = new ConcurrentHashMap<>();
    private final PersonIndeks personIndeks = new PersonIndeks();
    private final InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
    private final OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();

    @Override
    public Optional<OrganisasjonModell> getOrganisasjon(String orgnr) {
        return organisasjonIndeks.getModellForIdent(orgnr);
    }


    protected TestscenarioBuilderRepositoryImpl(BasisdataProvider basisdata) {
        this.basisdata = basisdata;
    }


    @Override
    public Map<String, Testscenario> getTestscenarios() {
        return scenarios;
    }

    @Override
    public Testscenario getTestscenario(String id) {
        return scenarios.get(id);
    }

    @Override
    public BasisdataProvider getBasisdata() {
        return basisdata;
    }

    public void indekser(TestscenarioImpl testScenario) {
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
    public LokalIdentIndeks getIdenter(String unikScenarioId) {
        return identer.computeIfAbsent(unikScenarioId, n -> new LokalIdentIndeks(n, basisdata.getIdentGenerator()));
    }

    @Override
    public PersonIndeks getPersonIndeks() {
        return personIndeks;
    }

    @Override
    public Optional<InntektYtelseModell> getInntektYtelseModell(String ident) {
        return inntektYtelseIndeks.getModellForIdent(ident);
    }

    @Override
    public Optional<InntektYtelseModell> getInntektYtelseModellFraAktørId(String aktørId) {
        return inntektYtelseIndeks.getModellForIdent(aktørId.substring(aktørId.length() - 11));
    }

    @Override
    public Boolean slettScenario(String id) {
        return scenarios.remove(id) != null;
    }

    @Override
    public Boolean endreTestscenario(Testscenario testscenario) {
        return null;
    }

}
