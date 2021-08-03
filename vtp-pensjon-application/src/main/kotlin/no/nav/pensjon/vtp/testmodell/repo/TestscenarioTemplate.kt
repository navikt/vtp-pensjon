package no.nav.pensjon.vtp.testmodell.repo

import no.nav.pensjon.vtp.testmodell.util.VariabelContainer
import java.io.Reader

/** En template for et test case inklusiv testdata.  */
interface TestscenarioTemplate {
    fun personopplysningReader(): Reader?

    fun inntektopplysningReader(rolle: String): Reader?

    fun organisasjonReader(): Reader?

    fun digitalkontaktinfoReader(): Reader?

    val vars: VariabelContainer
    fun getExpectedVars(): Set<TemplateVariable>
    val templateName: String

    /** default avleder id fra navn for enkelthets skyld.  */
    val templateKey: String
        get() {
            val templateNavn = templateName
            return templateNavn.replaceFirst("[-_].+$".toRegex(), "")
        }
}
