package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;

/** Indeks av alle testdata instanser. */
public class TestscenarioRepositoryImpl extends TestscenarioBuilderRepositoryImpl implements TestscenarioRepository {

    private final TestscenarioFraTemplateMapper mapper;

    public TestscenarioRepositoryImpl(PersonIndeks personIndeks, InntektYtelseIndeks inntektYtelseIndeks,
            OrganisasjonIndeks organisasjonIndeks,
            TestscenarioFraTemplateMapper mapper) {
        super(personIndeks, inntektYtelseIndeks, organisasjonIndeks);
        this.mapper = mapper;
    }


    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template) {
        return opprettTestscenario(template, Collections.emptyMap());
    }

    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        Testscenario testscenario = mapper.lagTestscenario(template, unikTestscenarioId, variables);
        indekser(testscenario);
        return testscenario;
    }

    @Override
    public Testscenario opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        Testscenario testscenario = mapper.lagTestscenarioFraJsonString(testscenarioJson, unikTestscenarioId, variables);
        indekser(testscenario);
        return testscenario;
    }


}
