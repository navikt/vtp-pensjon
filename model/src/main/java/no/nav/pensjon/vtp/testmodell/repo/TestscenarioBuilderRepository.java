package no.nav.pensjon.vtp.testmodell.repo;

import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;

import java.util.Map;
import java.util.Optional;

public interface TestscenarioBuilderRepository {

    InntektYtelseIndeks getInntektYtelseIndeks();

    PersonIndeks getPersonIndeks();

    Optional<OrganisasjonModell> getOrganisasjon(String orgnr);

    LokalIdentIndeks getIdenter(String unikScenarioId);

    BasisdataProvider getBasisdata();

    Map<String, Testscenario> getTestscenarios();

    Testscenario getTestscenario(String id);

    Boolean slettScenario(String id);
}
