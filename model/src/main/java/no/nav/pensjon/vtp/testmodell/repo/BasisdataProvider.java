package no.nav.pensjon.vtp.testmodell.repo;

import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.identer.IdentGenerator;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;

public interface BasisdataProvider {

    VirksomhetIndeks getVirksomhetIndeks();

    EnheterIndeks getEnheterIndeks();

    AnsatteIndeks getAnsatteIndeks();

    AdresseIndeks getAdresseIndeks();

    /** Genererer nye personidenter. */
    IdentGenerator getIdentGenerator();


}
