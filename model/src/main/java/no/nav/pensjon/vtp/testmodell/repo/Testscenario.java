package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;
import java.util.Set;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer;

public interface Testscenario {

    String getTemplateNavn();

    String getId();

    Personopplysninger getPersonopplysninger();

    OrganisasjonModeller getOrganisasjonModeller();

    VariabelContainer getVariabelContainer();

    InntektYtelseModell getSøkerInntektYtelse();

    InntektYtelseModell getAnnenpartInntektYtelse();

    Set<PersonArbeidsgiver> getPersonligArbeidsgivere();
}
