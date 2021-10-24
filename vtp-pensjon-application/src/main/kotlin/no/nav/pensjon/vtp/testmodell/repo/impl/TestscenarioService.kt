package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.testmodell.dkif.DkifRepository
import no.nav.pensjon.vtp.testmodell.dkif.dkifModellMapper
import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.pensjon.vtp.testmodell.load.TestscenarioLoad
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.pensjon.vtp.testmodell.repo.*
import org.springframework.stereotype.Component
import java.util.UUID.randomUUID

@Component
class TestscenarioService(
    private val mapper: TestscenarioFraTemplateMapper,
    private val testscenarioRepository: TestscenarioRepository,
    private val personIndeks: PersonIndeks,
    private val inntektYtelseIndeks: InntektYtelseIndeks,
    private val organisasjonRepository: OrganisasjonRepository,
    private val personModellRepository: PersonModellRepository,
    private val dkifRepository: DkifRepository,
    private val adresseIndeks: AdresseIndeks,
    private val identerIndeks: IdenterIndeks,
) {
    fun opprettTestscenario(template: TestscenarioTemplate) = opprettTestscenario(template, emptyMap())

    fun opprettTestscenario(
        template: TestscenarioTemplate,
        userSuppliedVariables: Map<String, String>
    ) = doSave(mapper.lagTestscenario(template, userSuppliedVariables))

    fun opprettTestscenarioFraJsonString(
        testscenarioJson: String,
        userSuppliedVariables: Map<String, String> = emptyMap()
    ) = doSave(mapper.lagTestscenarioFraJsonString(testscenarioJson, userSuppliedVariables))

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
    ) = Testscenario(
        testscenarioId, load.templateNavn,
        personopplysningerSave,
        load.søkerInntektYtelse,
        load.annenpartInntektYtelse,
        load.organisasjonModeller,
        load.dkifModeller,
        load.variabelContainer.getVars()
    )

    fun leggTil(bruker: PersonModell) = personModellRepository.save(bruker)

    fun findAll() = testscenarioRepository.findAll()

    fun getTestscenario(id: String) = testscenarioRepository.findById(id)

    fun slettScenario(id: String) = testscenarioRepository.deleteById(id)
}
