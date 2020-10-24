package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;

import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;

public interface TestscenarioBuilderRepository {

    InntektYtelseIndeks getInntektYtelseIndeks();

    OrganisasjonIndeks getOrganisasjonIndeks();

    PersonIndeks getPersonIndeks();

    LokalIdentIndeks getIdenter(String unikScenarioId);

    BasisdataProvider getBasisdata();

    Map<String, Testscenario> getTestscenarios();

    Testscenario getTestscenario(String id);

    Boolean slettScenario(String id);
}
