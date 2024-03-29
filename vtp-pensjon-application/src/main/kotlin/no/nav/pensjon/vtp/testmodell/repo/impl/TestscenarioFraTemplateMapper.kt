package no.nav.pensjon.vtp.testmodell.repo.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell
import no.nav.pensjon.vtp.testmodell.krr.DigitalKontaktinformasjon
import no.nav.pensjon.vtp.testmodell.load.PersonopplysningerTemplate
import no.nav.pensjon.vtp.testmodell.load.TestscenarioLoad
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModeller
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate
import no.nav.pensjon.vtp.testmodell.util.JsonMapper
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class TestscenarioFraTemplateMapper {
    fun lagTestscenario(template: TestscenarioTemplate, vars: Map<String, String>): TestscenarioLoad {
        return load(template, vars)
    }

    fun lagTestscenarioFraJsonString(testscenarioJson: String, vars: Map<String, String>): TestscenarioLoad {
        return loadTestscenarioFraJsonString(hentObjecetNodeForTestscenario(testscenarioJson), vars)
    }

    private fun hentObjecetNodeForTestscenario(testscenarioJson: String): ObjectNode {
        return try {
            ObjectMapper().readValue(testscenarioJson, ObjectNode::class.java)
        } catch (e: IOException) {
            throw IllegalArgumentException("Kunne ikke converte JSON streng til ObjectNode", e)
        }
    }

    private fun hentTemplateNavnFraJsonString(node: ObjectNode): String {
        return if (node.has("scenario-navn")) {
            val scenarioNavn = node["scenario-navn"]
            ObjectMapper().convertValue(scenarioNavn, String::class.java)
        } else {
            throw RuntimeException("Must include a template name")
        }
    }

    private fun loadTestscenarioFraJsonString(node: ObjectNode, overrideVars: Map<String, String>): TestscenarioLoad {
        val variabelContainer = VariabelContainer()
        val jsonMapper = JsonMapper(variabelContainer)
        if (node.has("vars")) {
            val vars = node["vars"]
            val defaultVars: Map<String, String> =
                ObjectMapper().convertValue(vars, object : TypeReference<Map<String, String>>() {})
            jsonMapper.addVars(defaultVars)
        }
        jsonMapper.addVars(overrideVars)
        val objectMapper = jsonMapper.lagObjectMapper()
        val organisasjonModeller = if (node.has("organisasjon")) {
            val organisasjon = node["organisasjon"]
            objectMapper.convertValue(organisasjon, OrganisasjonModeller::class.java).modeller
        } else {
            emptyList()
        }
        val soekerInntektYtelse = if (node.has("inntektytelse-søker")) {
            objectMapper.convertValue(node["inntektytelse-søker"], InntektYtelseModell::class.java)
        } else {
            null
        }
        val annenPartInntektYtelse = if (node.has("inntektytelse-annenpart")) {
            val inntektytelseAnnenpart = node["inntektytelse-annenpart"]
            objectMapper.convertValue(inntektytelseAnnenpart, InntektYtelseModell::class.java)
        } else {
            null
        }

        val personopplysninger = if (node.has("personopplysninger")) {
            val personopplysningerResult = node["personopplysninger"]
            objectMapper.convertValue(personopplysningerResult, PersonopplysningerTemplate::class.java)
        } else {
            throw IllegalArgumentException("Must include personopplysninger")
        }

        val digitalkontaktinfo = if (node.has("digitalkontaktinfo")) {
            val digitalkontaktinfoResult = node["digitalkontaktinfo"]
            objectMapper.convertValue(digitalkontaktinfoResult, DigitalKontaktinformasjon::class.java)
        } else {
            throw IllegalArgumentException("Must include digitalkontaktinfo")
        }

        return TestscenarioLoad(
            hentTemplateNavnFraJsonString(node),
            personopplysninger,
            soekerInntektYtelse,
            annenPartInntektYtelse,
            organisasjonModeller,
            digitalkontaktinfo,
            variabelContainer
        )
    }

    private fun load(template: TestscenarioTemplate, overrideVars: Map<String, String>): TestscenarioLoad {

        val variabelContainer = VariabelContainer()
        val objectMapper = JsonMapper(variabelContainer).apply {
            addVars(template.vars)
            addVars(overrideVars)
        }.lagObjectMapper()

        val personopplysninger = loadPersonopplysninger(template, objectMapper)
        val soekerInntektYtelse = loadInntektopplysning("søker", template, objectMapper)

        val annenPartInntektYtelse: InntektYtelseModell? = if (personopplysninger.annenPart != null) {
            loadInntektopplysning("annenpart", template, objectMapper)
        } else {
            null
        }
        val organisasjonModeller = loadOrganisasjonModeller(template, objectMapper)
        val digitalKontaktinformasjon = loadDigitalkontaktinfo(template, objectMapper)
        return TestscenarioLoad(
            template.templateName,
            personopplysninger,
            soekerInntektYtelse,
            annenPartInntektYtelse,
            organisasjonModeller,
            digitalKontaktinformasjon,
            variabelContainer
        )
    }

    private fun loadOrganisasjonModeller(
        template: TestscenarioTemplate,
        objectMapper: ObjectMapper
    ): List<OrganisasjonModell> {
        try {
            template.organisasjonReader().use { reader ->
                // detaljer
                return if (reader != null) {
                    objectMapper.readValue(reader, OrganisasjonModeller::class.java).modeller
                } else {
                    emptyList()
                }
            }
        } catch (e: IOException) {
            throw IllegalArgumentException("Kunne ikke lese organisasjon.json for scenario:$template", e)
        }
    }

    private fun loadInntektopplysning(
        rolle: String,
        template: TestscenarioTemplate,
        objectMapper: ObjectMapper
    ): InntektYtelseModell? {
        try {
            template.inntektopplysningReader(rolle).use { reader ->
                // detaljer
                return if (reader != null) {
                    objectMapper.readValue(reader, InntektYtelseModell::class.java)
                } else {
                    null
                }
            }
        } catch (e: IOException) {
            throw IllegalArgumentException("Kunne ikke lese inntektytelser-søker.json for scenario:$template", e)
        }
    }

    private fun loadPersonopplysninger(
        template: TestscenarioTemplate,
        objectMapper: ObjectMapper
    ): PersonopplysningerTemplate {
        try {
            template.personopplysningReader().use { reader ->
                return if (reader != null) {
                    objectMapper.readValue(reader, PersonopplysningerTemplate::class.java)
                } else {
                    throw RuntimeException("No personopplysninger for scenario: $template")
                }
            }
        } catch (e: IOException) {
            throw IllegalArgumentException("Kunne ikke lese personopplysning.json for scenario:$template", e)
        }
    }

    private fun loadDigitalkontaktinfo(
        template: TestscenarioTemplate,
        objectMapper: ObjectMapper
    ): DigitalKontaktinformasjon? {
        try {
            template.digitalkontaktinfoReader().use { reader ->
                return if (reader != null) {
                    objectMapper.readValue(reader, DigitalKontaktinformasjon::class.java)
                } else {
                    return null;
                }
            }
        } catch (e: IOException) {
            throw IllegalArgumentException("Kunne ikke lese digitalkontaktinfo.json for scenario:$template", e)
        }
    }
}
