package no.nav.pensjon.vtp.testmodell.scenario.sam

import no.nav.pensjon.vtp.mocks.tp.TjenestepensjonService
import no.nav.pensjon.vtp.testmodell.repo.Testscenario
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioService
import no.nav.pensjon.vtp.util.toLocalDate
import org.springframework.stereotype.Service

@Service
class SamTestScenarioService(
    private val templateRepository: TestscenarioTemplateRepository,
    private val tjenestepensjonService: TjenestepensjonService,
    private val testscenarioService: TestscenarioService,
) {
    fun opprettSamTestScenario(input: SamTestScenario) =
        templateRepository.finn("1000")?.let { template ->
            testscenarioService.opprettTestscenario(template, emptyMap()).let { testscenario ->
                val tjenestepensjon = input.tjenestepensjon?.let { tjenestepensjon ->
                    tjenestepensjonService.save(testscenario.personopplysninger.søker.ident, tjenestepensjon)
                }
                asSamTestScenario(testscenario, tjenestepensjon, tjenestepensjonService::getTpNrByTssEksternId)
            }
        }

    fun getSamTestScenario(testscenarioId: String) =
        testscenarioService.getTestscenario(testscenarioId)?.let {
            tjenestepensjonService.findByPid(it.personopplysninger.søker.ident)?.let { tjenestepensjon ->
                asSamTestScenario(it, tjenestepensjon, tjenestepensjonService::getTpNrByTssEksternId)
            }
        }

    companion object {
        private fun asSamTestScenario(
            testscenario: Testscenario,
            tjenestepensjon: no.nav.pensjon.vtp.mocks.tp.Tjenestepensjon?,
            tpNrLookup: (String) -> String,
        ) = SamTestScenario(
            testScenarioId = testscenario.id,
            pid = testscenario.personopplysninger.søker.ident,
            fornavn = testscenario.personopplysninger.søker.fornavn,
            etternavn = testscenario.personopplysninger.søker.etternavn,
            diskresjonskode = testscenario.personopplysninger.søker.diskresjonskode?.name,
            dodsdato = testscenario.personopplysninger.søker.dødsdato,
            tjenestepensjon = tjenestepensjon?.asDto(tpNrLookup)
        )

        private fun no.nav.pensjon.vtp.mocks.tp.Tjenestepensjon.asDto(tpNrLookup: (String) -> String) = Tjenestepensjon(
                forhold = forhold.map { forhold ->
                    Forhold(
                        tpNr = tpNrLookup(forhold.tssEksternId),
                        forholdId = forhold.forholdId!!.toLong(),
                        ytelser = forhold.ytelser.map { ytelse ->
                            Ytelse(
                                type = ytelse.ytelseKode!!,
                                ytelseId = ytelse.ytelseId.toLong(),
                                innmeldtFom = ytelse.innmeldtFom.toLocalDate(),
                                iverksattFom = ytelse.iverksattFom?.toLocalDate(),
                                iverksattTom = ytelse.iverksattTom?.toLocalDate(),
                            )
                        }.toSet()
                    )
                }.toSet()
            )
    }
}
