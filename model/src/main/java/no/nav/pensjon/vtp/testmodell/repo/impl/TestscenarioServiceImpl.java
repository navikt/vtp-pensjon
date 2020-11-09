package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.pensjon.vtp.testmodell.personopplysning.AnnenPartModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonNavn;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioService;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.util.TestdataUtil;

@Component
public class TestscenarioServiceImpl implements TestscenarioService {
    private static final Logger log = LoggerFactory.getLogger(TestscenarioServiceImpl.class);

    private final TestscenarioFraTemplateMapper mapper;
    private final TestscenarioRepository testscenarioRepository;
    private final PersonIndeks personIndeks;
    private final InntektYtelseIndeks inntektYtelseIndeks;
    private final OrganisasjonRepository organisasjonRepository;


    public TestscenarioServiceImpl(TestscenarioFraTemplateMapper mapper, TestscenarioRepository testscenarioRepository,
            PersonIndeks personIndeks, InntektYtelseIndeks inntektYtelseIndeks, OrganisasjonRepository organisasjonRepository) {
        this.mapper = mapper;
        this.testscenarioRepository = testscenarioRepository;
        this.personIndeks = personIndeks;
        this.inntektYtelseIndeks = inntektYtelseIndeks;
        this.organisasjonRepository = organisasjonRepository;
    }


    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template) {
        return opprettTestscenario(template, Collections.emptyMap());
    }

    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        return doSave(mapper.lagTestscenario(template, unikTestscenarioId, variables));
    }

    @Override
    public Testscenario opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        return doSave(mapper.lagTestscenarioFraJsonString(testscenarioJson, unikTestscenarioId, variables));
    }

    private Testscenario doSave(Testscenario testScenario) {
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
            for (FamilierelasjonModell fr : personopplysninger.getFamilierelasjoner()) {
                personIndeks.leggTil(fr.getTil());
            }

            personIndeks.indekserPersonopplysningerByIdent(personopplysninger);
            testScenario.getPersonligArbeidsgivere().forEach(personIndeks::leggTil);

            if (personopplysninger.getSøker().getIdent() != null && testScenario.getSøkerInntektYtelse() != null) {
                inntektYtelseIndeks.leggTil(personopplysninger.getSøker().getIdent(), testScenario.getSøkerInntektYtelse());
            }
            if (personopplysninger.getAnnenPart() != null) {
                inntektYtelseIndeks.leggTil(personopplysninger.getAnnenPart().getIdent(), testScenario.getAnnenpartInntektYtelse());
            }
        }

        OrganisasjonModeller organisasjonModeller = testScenario.getOrganisasjonModeller();
        if (organisasjonModeller != null) {
            List<OrganisasjonModell> modeller = organisasjonModeller.getModeller();
            organisasjonRepository.saveAll(modeller);
        }

        return testscenarioRepository.save(testScenario);
    }

    @Override
    public Stream<Testscenario> findAll() {
        return testscenarioRepository.findAll();
    }

    @Override
    public Optional<Testscenario> getTestscenario(String id) {
        return testscenarioRepository.findById(id);
    }

    @Override
    public void slettScenario(String id) {
        testscenarioRepository.delete(id);
    }
}
