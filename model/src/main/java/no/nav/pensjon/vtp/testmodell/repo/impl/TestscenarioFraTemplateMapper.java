package no.nav.pensjon.vtp.testmodell.repo.impl;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.load.PersonopplysningerTemplate;
import no.nav.pensjon.vtp.testmodell.load.TestscenarioLoad;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.util.JsonMapper;
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer;

@Component
public class TestscenarioFraTemplateMapper {
    public TestscenarioLoad lagTestscenario(TestscenarioTemplate template, Map<String, String> vars) {
        return load(template, vars);
    }

    public TestscenarioLoad lagTestscenarioFraJsonString(String testscenarioJson, Map<String, String> vars) {
        ObjectNode node = hentObjecetNodeForTestscenario(testscenarioJson);
        return loadTestscenarioFraJsonString(node, vars);
    }

    private ObjectNode hentObjecetNodeForTestscenario(String testscenarioJson) {
        ObjectNode node;
        try {
            node = new ObjectMapper().readValue(testscenarioJson, ObjectNode.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke converte JSON streng til ObjectNode", e);
        }
        return node;
    }

    private String hentTemplateNavnFraJsonString(ObjectNode node) {
        if (node.has("scenario-navn")) {
            JsonNode scenarioNavn = node.get("scenario-navn");
            return new ObjectMapper().convertValue(scenarioNavn, String.class);
        } else {
            throw new RuntimeException("Must include a template name");
        }
    }


    private TestscenarioLoad loadTestscenarioFraJsonString(ObjectNode node, Map<String, String> overrideVars) {
        final VariabelContainer variabelContainer = new VariabelContainer();
        final PersonopplysningerTemplate personopplysninger;
        final InntektYtelseModell søkerInntektYtelse;
        final InntektYtelseModell annenPartInntektYtelse;
        final List<OrganisasjonModell> organisasjonModeller;

        JsonMapper jsonMapper = new JsonMapper(variabelContainer);
        if (node.has("vars")) {
            JsonNode vars = node.get("vars");
            Map<String,String> defaultVars = new ObjectMapper().convertValue(vars, new TypeReference<>() {
            });
            jsonMapper.addVars(defaultVars);
        }

        jsonMapper.addVars(overrideVars);
        ObjectMapper objectMapper = jsonMapper.lagObjectMapper();

        if(node.has("organisasjon")){
            JsonNode organisasjon = node.get("organisasjon");
            organisasjonModeller = objectMapper.convertValue(organisasjon, OrganisasjonModeller.class).getModeller();
        } else {
            organisasjonModeller = emptyList();
        }

        if(node.has("inntektytelse-søker")){
            JsonNode inntektytelseSøker = node.get("inntektytelse-søker");
            søkerInntektYtelse = objectMapper.convertValue(inntektytelseSøker, InntektYtelseModell.class);
        } else {
            søkerInntektYtelse = null;
        }

        if(node.has("inntektytelse-annenpart")){
            JsonNode inntektytelseAnnenpart = node.get("inntektytelse-annenpart");
            annenPartInntektYtelse = objectMapper.convertValue(inntektytelseAnnenpart, InntektYtelseModell.class);
        } else {
            annenPartInntektYtelse = null;
        }

        if(node.has("personopplysninger")){
            JsonNode personopplysningerResult = node.get("personopplysninger");
            personopplysninger = objectMapper.convertValue(personopplysningerResult, PersonopplysningerTemplate.class);
        } else {
            throw new IllegalArgumentException("Must include personopplysninger");
        }

        return new TestscenarioLoad(
                hentTemplateNavnFraJsonString(node),
                personopplysninger,
                søkerInntektYtelse,
                annenPartInntektYtelse,
                organisasjonModeller,
                variabelContainer
        );
    }

    private TestscenarioLoad load(TestscenarioTemplate template, Map<String, String> overrideVars) {
        final VariabelContainer variabelContainer = new VariabelContainer();
        final PersonopplysningerTemplate personopplysninger;
        final InntektYtelseModell søkerInntektYtelse;
        final InntektYtelseModell annenPartInntektYtelse;
        final List<OrganisasjonModell> organisasjonModeller;

        JsonMapper jsonMapper = new JsonMapper(variabelContainer);
        jsonMapper.addVars(template.getDefaultVars());
        jsonMapper.addVars(overrideVars);
        ObjectMapper objectMapper = jsonMapper.lagObjectMapper();
        try (Reader reader = template.personopplysningReader()) {
            if (reader != null) {
                personopplysninger = objectMapper.readValue(reader, PersonopplysningerTemplate.class);
            } else {
                throw new RuntimeException("No personopplysninger for scenario: " + template);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese personopplysning.json for scenario:" + template, e);
        }

        try (Reader reader = template.inntektopplysningReader("søker")) {
            // detaljer
            if (reader != null) {
                søkerInntektYtelse = objectMapper.readValue(reader, InntektYtelseModell.class);
            } else {
                søkerInntektYtelse = null;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese inntektytelser-søker.json for scenario:" + template, e);
        }

        if (personopplysninger.getAnnenPart() != null) {
            try (Reader reader = template.inntektopplysningReader("annenpart")) {
                // detaljer
                if (reader != null) {
                    annenPartInntektYtelse = objectMapper.readValue(reader, InntektYtelseModell.class);
                } else {
                    annenPartInntektYtelse = null;
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Kunne ikke lese inntektytelser-annenpart.json for scenario:" + template, e);
            }
        } else {
            annenPartInntektYtelse = null;
        }

        try (Reader reader = template.organisasjonReader()) {
            // detaljer
            if (reader != null) {
                organisasjonModeller = objectMapper.readValue(reader, OrganisasjonModeller.class).getModeller();
            } else {
                organisasjonModeller = emptyList();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Kunne ikke lese organisasjon.json for scenario:" + template, e);
        }

        return new TestscenarioLoad(
                template.getTemplateNavn(),
                personopplysninger,
                søkerInntektYtelse,
                annenPartInntektYtelse,
                organisasjonModeller,
                variabelContainer
        );
    }
}
