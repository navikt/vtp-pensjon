package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.repo.BasisdataProvider;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;

/** Indeks av alle testdata instanser. */
public class TestscenarioRepositoryImpl extends TestscenarioBuilderRepositoryImpl implements TestscenarioRepository {

    public TestscenarioRepositoryImpl(BasisdataProvider basisdata, PersonIndeks personIndeks, InntektYtelseIndeks inntektYtelseIndeks,
            OrganisasjonIndeks organisasjonIndeks) {
        super(basisdata, personIndeks, inntektYtelseIndeks, organisasjonIndeks);
    }


    @Override
    public TestscenarioImpl opprettTestscenario(TestscenarioTemplate template) {
        return opprettTestscenario(template, Collections.emptyMap());
    }

    @Override
    public TestscenarioImpl opprettTestscenario(TestscenarioTemplate template, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        TestscenarioFraTemplateMapper mapper = new TestscenarioFraTemplateMapper(this);
        return mapper.lagTestscenario(template, unikTestscenarioId, variables);
    }

    @Override
    public TestscenarioImpl opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        TestscenarioFraTemplateMapper mapper = new TestscenarioFraTemplateMapper(this);
        return mapper.lagTestscenarioFraJsonString(testscenarioJson, unikTestscenarioId, variables);
    }


}
