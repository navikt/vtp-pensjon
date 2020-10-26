package no.nav.pensjon.vtp.testmodell.repo.impl;

import static java.util.Objects.isNull;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.util.FindTemplateVariables;
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import no.nav.pensjon.vtp.testmodell.repo.TemplateVariable;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;

public class FileTestscenarioTemplate implements TestscenarioTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(FileTestscenarioTemplate.class);

    public static final String PERSONOPPLYSNING_JSON_FILE = "personopplysning.json";
    public static final String ORGANISASJON_JSON_FILE = "organisasjon.json";

    private final VariabelContainer vars = new VariabelContainer();

    private final String templateDir;
    private final String templateName;

    public FileTestscenarioTemplate(String templateDir, String templateName, VariabelContainer vars) {
        this.templateDir = notEmpty(templateDir, "templateDir");
        this.templateName = notEmpty(templateName, "templateName");
        this.vars.putAll(vars);
    }

    @Override
    public String toString() {
        return "FileTestscenarioTemplate{" +
                ", templateName='" + templateName + '\'' +
                '}';
    }

    private String notEmpty(String string, String name) {
        if (isNull(string)) {
            throw new NullPointerException("Input argument '" + name + "' was null");
        } else if (isEmpty(string)) {
            throw new IllegalArgumentException("Input argument '" + name + "' was empty");
        } else {
            return string;
        }
    }

    @Override
    public String getTemplateNavn() {
        return templateName;
    }

    @Override
    public VariabelContainer getDefaultVars() {
        return vars;
    }

    @Override
    public Set<TemplateVariable> getExpectedVars() {
        try (Reader personopplysningReader = personopplysningReader();
                Reader søkerInntektopplysningReader = inntektopplysningReader("søker");
                Reader annenpartInntektopplysningReader = inntektopplysningReader("annenpart");
                Reader organisasjonsReader = organisasjonReader()) {
            FindTemplateVariables finder = new FindTemplateVariables();

            finder.scanForVariables(Personopplysninger.class, personopplysningReader);
            finder.scanForVariables(InntektYtelseModell.class, søkerInntektopplysningReader);
            finder.scanForVariables(InntektYtelseModell.class, annenpartInntektopplysningReader);
            finder.scanForVariables(OrganisasjonModell.class, organisasjonsReader);

            return finder.getDiscoveredVariables();

        } catch (IOException e) {
            throw new IllegalStateException("Kunne ikke parse json", e);
        }
    }

    @Override
    public Reader personopplysningReader() throws IOException {
        LOG.info("Leser personopplysninger fra mappe " + templateDir + ", fil: " + PERSONOPPLYSNING_JSON_FILE);
        return asReader(templateDir + PERSONOPPLYSNING_JSON_FILE);
    }

    @Override
    public Reader inntektopplysningReader(String rolle) throws IOException {
        return asReader(templateDir + String.format("inntektytelse-%s.json", rolle));
    }

    @Override
    public Reader organisasjonReader() throws IOException {
        return asReader(templateDir + ORGANISASJON_JSON_FILE);
    }

    private Reader asReader(String path) throws IOException {
        Resource resource = new UrlResource(path);
        return resource.exists() ? new InputStreamReader(resource.getInputStream()) : null;
    }
}
