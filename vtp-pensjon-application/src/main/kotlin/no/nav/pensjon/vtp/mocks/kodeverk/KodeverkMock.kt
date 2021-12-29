package no.nav.pensjon.vtp.mocks.kodeverk

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.configuration.graphql.model.mapBostedsadresser
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

data class Beskrivelse(val term: String, val tekst: String = term)
data class Betydning(val beskrivelser: Map<String, Beskrivelse>, val gyldigFra: LocalDate = LocalDate.now().minusYears(10), val gyldigTil: LocalDate = LocalDate.now().plusYears(10))
data class KodeBetydninger(val betydninger: Map<String, List<Betydning>>)

@RestController
@Tag(name = "Kodeverk")
@RequestMapping("rest/kodeverk/v1/kodeverk")
class KodeverkMock(private val personModellRepository: PersonModellRepository) {

    @GetMapping("{kodeverk}/koder/betydninger")
    fun getKodeverk(
        @PathVariable("kodeverk") kodeverk: String
    ): KodeBetydninger =
        when (kodeverk) {
            "Postnummer" ->
                postnummer()

            else ->
                throw NotImplementedException()
        }

    private fun postnummer(): KodeBetydninger =
        personModellRepository.findAll().flatMap { person ->
            person.adresser.mapBostedsadresser().map { it.vegadresse?.postnummer }
        }.filterNotNull()
            .distinct()
            .associate {
                it to listOf(Betydning(mapOf("nb" to Beskrivelse("HUTTAHEI"))))
            }.let { KodeBetydninger(it) }
}
