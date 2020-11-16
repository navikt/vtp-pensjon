package no.nav.pensjon.vtp.testmodell.repo.impl;

import static java.util.Optional.ofNullable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.load.TestscenarioLoad;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.BrukerModellRepository;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioService;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;

@Component
public class TestscenarioServiceImpl implements TestscenarioService {

    private final TestscenarioFraTemplateMapper mapper;
    private final TestscenarioRepository testscenarioRepository;
    private final PersonIndeks personIndeks;
    private final InntektYtelseIndeks inntektYtelseIndeks;
    private final OrganisasjonRepository organisasjonRepository;
    private final BrukerModellRepository brukerModellRepository;
    private final AdresseIndeks adresseIndeks;
    private final IdenterIndeks identerIndeks;


    public TestscenarioServiceImpl(TestscenarioFraTemplateMapper mapper, TestscenarioRepository testscenarioRepository,
            PersonIndeks personIndeks, InntektYtelseIndeks inntektYtelseIndeks, OrganisasjonRepository organisasjonRepository,
            BrukerModellRepository brukerModellRepository, AdresseIndeks adresseIndeks, IdenterIndeks identerIndeks) {
        this.mapper = mapper;
        this.testscenarioRepository = testscenarioRepository;
        this.personIndeks = personIndeks;
        this.inntektYtelseIndeks = inntektYtelseIndeks;
        this.organisasjonRepository = organisasjonRepository;
        this.brukerModellRepository = brukerModellRepository;
        this.adresseIndeks = adresseIndeks;
        this.identerIndeks = identerIndeks;
    }


    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template) {
        return opprettTestscenario(template, Collections.emptyMap());
    }

    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> variables) {
        return doSave(mapper.lagTestscenario(template, variables));
    }

    @Override
    public Testscenario opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> variables) {
        return doSave(mapper.lagTestscenarioFraJsonString(testscenarioJson, variables));
    }

    private Testscenario doSave(TestscenarioLoad testScenario) {
        final String testscenarioId = UUID.randomUUID().toString();
        final Mapper mapper = new Mapper(identerIndeks.getIdenter(testscenarioId), adresseIndeks, testScenario.getVariabelContainer());

        Personopplysninger personopplysninger = mapper.mapFromLoad(testScenario.getPersonopplysninger());

        leggTil(personopplysninger.getSøker());

        ofNullable(personopplysninger.getAnnenPart()).ifPresent(this::leggTil);
        personIndeks.indekserPersonopplysningerByIdent(personopplysninger);

        ofNullable(testScenario.getSøkerInntektYtelse())
                .ifPresent(inntektYtelseModell -> inntektYtelseIndeks.leggTil(personopplysninger.getSøker().getIdent(), inntektYtelseModell));

        ofNullable(personopplysninger.getAnnenPart())
                .ifPresent(annenPart ->
                    ofNullable(testScenario.getAnnenpartInntektYtelse())
                            .ifPresent(inntektYtelseModell -> inntektYtelseIndeks.leggTil(annenPart.getIdent(), inntektYtelseModell))
                );

        organisasjonRepository.saveAll(testScenario.getOrganisasjonModeller());

        return testscenarioRepository.save(mapToTestscenario(testScenario, testscenarioId, personopplysninger));
    }

    private Testscenario mapToTestscenario(final TestscenarioLoad load, final String testscenarioId, final Personopplysninger personopplysningerSave) {
        return new Testscenario(
                load.getTemplateNavn(),
                testscenarioId,
                personopplysningerSave,
                load.getSøkerInntektYtelse(),
                load.getAnnenpartInntektYtelse(),
                load.getOrganisasjonModeller(),
                load.getVariabelContainer().getVars()
        );
    }

    public synchronized void leggTil(PersonModell bruker) {
        if (bruker == null) {
            // quiet escape
            return;
        }

        brukerModellRepository.save(bruker);
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
