package no.nav.pensjon.vtp.testmodell.repo;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer;

public interface Testscenario {

    String getTemplateNavn();

    String getId();

    Personopplysninger getPersonopplysninger();

    VariabelContainer getVariabelContainer();

    InntektYtelseModell getSøkerInntektYtelse();

    InntektYtelseModell getAnnenpartInntektYtelse();

}
