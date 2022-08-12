package no.nav.pensjon.vtp.testmodell.scenario.pensjon

import no.nav.pensjon.vtp.common.testscenario.VtpPensjonTestScenario
import no.nav.pensjon.vtp.testmodell.repo.Testscenario
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioService
import org.springframework.stereotype.Service

@Service
class VtpPensjonTestScenarioService(
    private val templateRepository: TestscenarioTemplateRepository,
    private val testscenarioService: TestscenarioService,
) {
    fun opprettVtpPensjonTestScenario(input: VtpPensjonTestScenario) =
        templateRepository.finn("1000")?.let {
            val map = mutableMapOf("for1" to input.pid!!)
            input.foedselsdato?.let { fodselsdato -> map.put ("for1_foedselsdato", fodselsdato.toString())}
            testscenarioService.opprettTestscenario(it, map).asVtpPensjonTestScenario()
        }

    fun getSamTestScenario(testscenarioId: String) =
        testscenarioService.getTestscenario(testscenarioId)?.asVtpPensjonTestScenario()

    companion object {
        private fun Testscenario.asVtpPensjonTestScenario() = VtpPensjonTestScenario(
            testScenarioId = id,
            pid = personopplysninger.søker.ident,
            foedselsdato = personopplysninger.søker.fødselsdato,
            fornavn = personopplysninger.søker.fornavn,
            etternavn = personopplysninger.søker.etternavn,
            diskresjonskode = personopplysninger.søker.diskresjonskode?.name,
            dodsdato = personopplysninger.søker.dødsdato,
        )
    }
}
