package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.pensjon.vtp.testmodell.load.TestscenarioLoad
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import no.nav.pensjon.vtp.testmodell.repo.Testscenario
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioService
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate
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
    private val adresseIndeks: AdresseIndeks,
    private val identerIndeks: IdenterIndeks

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
}
