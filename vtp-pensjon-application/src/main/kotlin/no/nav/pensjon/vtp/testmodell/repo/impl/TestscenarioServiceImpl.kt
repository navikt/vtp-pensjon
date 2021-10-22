package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.mocks.tp.TjenestepensjonService
import no.nav.pensjon.vtp.testmodell.dkif.DkifRepository
import no.nav.pensjon.vtp.testmodell.dkif.dkifModellMapper
import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.pensjon.vtp.testmodell.load.TestscenarioLoad
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.pensjon.vtp.testmodell.repo.*
import no.nav.pensjon.vtp.testmodell.scenario.pensjon.Forhold
import no.nav.pensjon.vtp.testmodell.scenario.pensjon.PensjonTestScenario
import no.nav.pensjon.vtp.testmodell.scenario.pensjon.Tjenestepensjon
import no.nav.pensjon.vtp.testmodell.scenario.pensjon.Ytelse
import no.nav.pensjon.vtp.util.toLocalDate
import org.springframework.stereotype.Component
import java.util.UUID.randomUUID

@Component
class TestscenarioServiceImpl(
    private val mapper: TestscenarioFraTemplateMapper,
    private val testscenarioRepository: TestscenarioRepository,
    private val personIndeks: PersonIndeks,
    private val inntektYtelseIndeks: InntektYtelseIndeks,
    private val organisasjonRepository: OrganisasjonRepository,
    private val personModellRepository: PersonModellRepository,
    private val dkifRepository: DkifRepository,
    private val adresseIndeks: AdresseIndeks,
    private val identerIndeks: IdenterIndeks,
    private val templateRepository: TestscenarioTemplateRepository,
    private val tjenestepensjonService: TjenestepensjonService,
) : TestscenarioService {
    override fun opprettTestscenario(template: TestscenarioTemplate): Testscenario {
        return opprettTestscenario(template, emptyMap())
    }

    override fun opprettTestscenario(
        template: TestscenarioTemplate,
        userSuppliedVariables: Map<String, String>
    ): Testscenario {
        return doSave(mapper.lagTestscenario(template, userSuppliedVariables))
    }

    override fun opprettTestscenarioFraJsonString(
        testscenarioJson: String,
        userSuppliedVariables: Map<String, String>
    ): Testscenario {
        return doSave(mapper.lagTestscenarioFraJsonString(testscenarioJson, userSuppliedVariables))
    }

    private fun doSave(testScenario: TestscenarioLoad): Testscenario {
        val testscenarioId = randomUUID().toString()
        val mapper = Mapper(identerIndeks.getIdenter(testscenarioId), adresseIndeks, testScenario.variabelContainer)
        val personopplysninger = mapper.mapFromLoad(testScenario.personopplysninger)

        leggTil(personopplysninger.søker)
        personopplysninger.annenPart?.let { leggTil(it) }

        personIndeks.indekserPersonopplysningerByIdent(personopplysninger)
        testScenario.søkerInntektYtelse
            ?.let { inntektYtelseIndeks.leggTil(personopplysninger.søker.ident, it) }

        personopplysninger.annenPart
            ?.let { (ident) ->
                testScenario.annenpartInntektYtelse
                    ?.let { inntektYtelseIndeks.leggTil(ident, it) }
            }

        organisasjonRepository.saveAll(testScenario.organisasjonModeller)

        dkifRepository.saveAll(dkifModellMapper(testScenario.dkifModeller, identerIndeks.getIdenter(testscenarioId)))

        return testscenarioRepository.save(mapToTestscenario(testScenario, testscenarioId, personopplysninger))
    }

    private fun mapToTestscenario(
        load: TestscenarioLoad,
        testscenarioId: String,
        personopplysningerSave: Personopplysninger
    ): Testscenario {
        return Testscenario(
            testscenarioId, load.templateNavn,
            personopplysningerSave,
            load.søkerInntektYtelse,
            load.annenpartInntektYtelse,
            load.organisasjonModeller,
            load.dkifModeller,
            load.variabelContainer.getVars()
        )
    }

    fun leggTil(bruker: PersonModell) {
        personModellRepository.save(bruker)
    }

    override fun findAll(): List<Testscenario> {
        return testscenarioRepository.findAll()
    }

    override fun getTestscenario(id: String): Testscenario? {
        return testscenarioRepository.findById(id)
    }

    override fun slettScenario(id: String) {
        testscenarioRepository.deleteById(id)
    }

    override fun opprettPensjonTestScenario(templateId: String, input: PensjonTestScenario): PensjonTestScenario? {
        return templateRepository.finn(templateId)
            ?.let { template ->
                val testscenario = opprettTestscenario(template, emptyMap())
                val person = testscenario.personopplysninger.søker
                input.tjenestepensjon?.let { tjenestepensjon ->
                    tjenestepensjonService.save(person.ident, tjenestepensjon)
                }
                getPensjonTestScenario(testscenario.personopplysninger.søker)
            }
    }

    override fun getPensjonTestScenario(person: PersonModell): PensjonTestScenario {
        val tjenestepensjon: no.nav.pensjon.vtp.mocks.tp.Tjenestepensjon? = tjenestepensjonService.findByPid(person.ident)

        return PensjonTestScenario(
            fnr = person.ident,
            fornavn = person.fornavn,
            etternavn = person.etternavn,
            diskresjonskode = person.diskresjonskode?.name,
            dodsdato = person.dødsdato,
            tjenestepensjon = Tjenestepensjon(
                forhold = tjenestepensjon?.forhold?.map { forhold ->
                    Forhold(
                        tpNr = forhold.tpnr,
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
                }?.toSet()
                    ?: emptySet()
            )
        )
    }
}
