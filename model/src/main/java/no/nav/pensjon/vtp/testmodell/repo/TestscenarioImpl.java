package no.nav.pensjon.vtp.testmodell.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonArbeidsgiver;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer;
import no.nav.pensjon.vtp.testmodell.virksomhet.ScenarioVirksomheter;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;

public class TestscenarioImpl implements Testscenario {

    private final String templateNavn;

    /** identity cache for dette scenario. Medfører at identer kan genereres dynamisk basert på lokal id referanse i scenarioet. */
    private final LokalIdentIndeks identer;

    private AdresseIndeks adresseIndeks;

    private Personopplysninger personopplysninger;

    private InntektYtelseModell søkerInntektYtelse;

    private InntektYtelseModell annenpartInntektYtelse;

    private final OrganisasjonModeller organisasjonModeller = new OrganisasjonModeller();

    private final ScenarioVirksomheter scenarioVirksomheter;

    /** Unik testscenario id. */
    private final String id;

    private final VariabelContainer vars = new VariabelContainer();

    public TestscenarioImpl(String templateNavn, String id, TestscenarioBuilderRepository scenarioIndeks, final VirksomhetIndeks virksomhetIndeks) {
        this.templateNavn = templateNavn;
        this.id = id;

        this.scenarioVirksomheter = new ScenarioVirksomheter(this.templateNavn, virksomhetIndeks);

        this.identer = scenarioIndeks.getIdenter(getId());
    }

    @Override
    public String getTemplateNavn() {
        return templateNavn;
    }

    @Override
    public String getId() {
        return id;
    }

    public LokalIdentIndeks getIdenter() {
        return identer;
    }

    public AdresseIndeks getAdresseIndeks() {
        return adresseIndeks;
    }

    public void setAdresseIndeks(AdresseIndeks adresseIndeks) {
        this.adresseIndeks = adresseIndeks;
    }

    public void setPersonopplysninger(Personopplysninger personopplysninger) {
        this.personopplysninger = personopplysninger;
        personopplysninger.setIdenter(identer);
    }

    @Override
    public Personopplysninger getPersonopplysninger() {
        return this.personopplysninger;
    }

    public OrganisasjonModeller getOrganisasjonModeller() {
        return organisasjonModeller;
    }

    public void leggTil(OrganisasjonModell organisasjonModell) {
        organisasjonModeller.leggTil(organisasjonModell);
    }

    public ScenarioVirksomheter getVirksomheter() {
        return scenarioVirksomheter;
    }

    @Override
    public VariabelContainer getVariabelContainer() {
        return vars;
    }

    @Override
    public InntektYtelseModell getSøkerInntektYtelse() {
        return søkerInntektYtelse;
    }

    @Override
    public InntektYtelseModell getAnnenpartInntektYtelse() {
        return annenpartInntektYtelse;
    }

    public void setSøkerInntektYtelse(InntektYtelseModell inntektYtelse) {
        this.søkerInntektYtelse = inntektYtelse;
    }

    public void setAnnenpartInntektYtelse(InntektYtelseModell inntektYtelse) {
        this.annenpartInntektYtelse = inntektYtelse;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "<template, " + templateNavn + ", id=" + id + ">";
    }

    /** Returnerer alle personlige arbeidsgivere (fra søker og annen part). */
    public Set<PersonArbeidsgiver> getPersonligArbeidsgivere() {
        ArrayList<PersonArbeidsgiver> result = new ArrayList<>();
        result.addAll(getPersonArbeidsgivere(getSøkerInntektYtelse()));
        result.addAll(getPersonArbeidsgivere(getAnnenpartInntektYtelse()));
        return Set.copyOf(result);
    }

    private List<PersonArbeidsgiver> getPersonArbeidsgivere(InntektYtelseModell iyModell) {
        if (iyModell == null || iyModell.getInntektskomponentModell() == null || iyModell.getInntektskomponentModell().getInntektsperioder() == null) {
            return Collections.emptyList();
        }
        return iyModell.getInntektskomponentModell().getInntektsperioder().stream()
            .map(Inntektsperiode::getPersonligArbeidsgiver)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
