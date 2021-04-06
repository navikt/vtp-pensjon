package no.nav.pensjon.vtp.mocks.oppgave.sak

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1.GsakRepo
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@Api(tags = ["Gsak repository"])
@RequestMapping("/rest/api/sak")
class SakRestTjeneste(private val personModellRepository: PersonModellRepository, private val gsakRepo: GsakRepo) {
    @PostMapping
    @ApiOperation(value = "", notes = "Lager nytt saksnummer fra sekvens", response = OpprettSakResponseDTO::class)
    fun foreldrepengesoknadErketype(@RequestBody requestDTO: OpprettSakRequestDTO): OpprettSakResponseDTO {
        if (requestDTO.lokalIdent.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Request mangler påkrevde verdier")
        }

        val sak = gsakRepo.leggTilSak(
            requestDTO.lokalIdent
                .mapNotNull { ident: String ->
                    personModellRepository.findById(ident)
                },
            requestDTO.fagområde,
            requestDTO.fagsystem,
            requestDTO.sakstype
        )
        return OpprettSakResponseDTO(sak.sakId)
    }
}
