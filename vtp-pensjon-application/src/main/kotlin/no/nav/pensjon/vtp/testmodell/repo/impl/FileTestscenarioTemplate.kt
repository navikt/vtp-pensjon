package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger
import no.nav.pensjon.vtp.testmodell.repo.TemplateVariable
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate
import no.nav.pensjon.vtp.testmodell.util.FindTemplateVariables
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.util.ObjectUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader

class FileTestscenarioTemplate(
    val templateDir: String,
    override val templateName: String,
    override val vars: VariabelContainer = VariabelContainer()
) : TestscenarioTemplate {

    override fun toString(): String {
        return "FileTestscenarioTemplate{" +
            ", templateName='" + templateName + '\'' +
            '}'
    }

    private fun notEmpty(string: String, name: String) {
        if (ObjectUtils.isEmpty(string)) {
            throw IllegalArgumentException("Input argument '$name' was empty")
        }
    }

    override fun getExpectedVars(): Set<TemplateVariable> {
        try {
            personopplysningReader().use { personopplysningReader ->
                inntektopplysningReader("søker").use { søkerInntektopplysningReader ->
                    inntektopplysningReader("annenpart").use { annenpartInntektopplysningReader ->
                        organisasjonReader().use { organisasjonsReader ->
                            val finder = FindTemplateVariables()
                            finder.scanForVariables(Personopplysninger::class.java, personopplysningReader)
                            finder.scanForVariables(InntektYtelseModell::class.java, søkerInntektopplysningReader)
                            finder.scanForVariables(InntektYtelseModell::class.java, annenpartInntektopplysningReader)
                            finder.scanForVariables(OrganisasjonModell::class.java, organisasjonsReader)
                            return finder.discoveredVariables
                        }
                    }
                }
            }
        } catch (e: IOException) {
            throw IllegalStateException("Kunne ikke parse json", e)
        }
    }

    override fun personopplysningReader(): Reader? {
        return asReader(templateDir + PERSONOPPLYSNING_JSON_FILE)
    }

    override fun inntektopplysningReader(rolle: String): Reader? {
        return asReader(templateDir + "inntektytelse-$rolle.json")
    }

    override fun organisasjonReader(): Reader? {
        return asReader(templateDir + ORGANISASJON_JSON_FILE)
    }

    private fun asReader(path: String): Reader? {
        val resource: Resource = UrlResource(path)
        return if (resource.exists()) InputStreamReader(resource.inputStream) else null
    }

    companion object {
        const val PERSONOPPLYSNING_JSON_FILE = "personopplysning.json"
        const val ORGANISASJON_JSON_FILE = "organisasjon.json"
    }

    init {
        notEmpty(templateDir, "templateDir")
        notEmpty(templateName, "templateName")
    }
}
