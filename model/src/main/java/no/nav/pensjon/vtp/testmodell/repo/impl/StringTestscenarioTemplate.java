package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.repo.TemplateVariable;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.util.FindTemplateVariables;
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer;

public class StringTestscenarioTemplate implements TestscenarioTemplate {

    private final VariabelContainer vars = new VariabelContainer();
    private final String personopplysningTemplate;
    private final String søkerInntektYtelseTemplate;
    private final String organisasjonTemplate;
    private final String templateNavn;
    private final String annenpartInntektYtelseTemplate;

    public StringTestscenarioTemplate(String templateNavn, String personopplysningTemplate, String søkerInntektYtelseTemplate, String organisasjonTemplate) {
        this(templateNavn, personopplysningTemplate, søkerInntektYtelseTemplate, null, organisasjonTemplate, Collections.emptyMap());
    }

    public StringTestscenarioTemplate(String templateNavn, String personopplysningTemplate, String søkerInntektYtelseTemplate,
                                      String annenpartInntektYtelseTemplate, String organisasjonTemplate, Map<String, String> vars) {
        this.templateNavn = templateNavn;
        this.personopplysningTemplate = personopplysningTemplate;
        this.søkerInntektYtelseTemplate = søkerInntektYtelseTemplate;
        this.annenpartInntektYtelseTemplate = annenpartInntektYtelseTemplate;
        this.organisasjonTemplate = organisasjonTemplate;

        this.vars.putAll(vars);
    }

    @Override
    public String getTemplateNavn() {
        return templateNavn;
    }

    @Override
    public VariabelContainer getDefaultVars() {
        return vars;
    }

    @Override
    public Reader personopplysningReader() {
        return personopplysningTemplate == null ? null : new StringReader(personopplysningTemplate);
    }

    @Override
    public Reader inntektopplysningReader(String rolle) {
        if (søkerInntektYtelseTemplate == null) {
            return null;
        }

        switch (rolle) {
            case "søker":
                return new StringReader(søkerInntektYtelseTemplate);
            case "annenpart":
                return new StringReader(annenpartInntektYtelseTemplate);
            default:
                return null;
        }
    }

    @Override
    public Reader organisasjonReader() {
        return null == organisasjonTemplate ? null : new StringReader(organisasjonTemplate);
    }

    @Override
    public Set<TemplateVariable> getExpectedVars() {
        FindTemplateVariables finder = new FindTemplateVariables();
        finder.scanForVariables(Personopplysninger.class, personopplysningReader());
        finder.scanForVariables(InntektYtelseModell.class, inntektopplysningReader("søker"));
        finder.scanForVariables(InntektYtelseModell.class, inntektopplysningReader("annenpart"));
        return finder.getDiscoveredVariables();
    }

}
