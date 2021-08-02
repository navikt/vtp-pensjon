package no.nav.pensjon.vtp.mocks.dkif

import io.swagger.annotations.Api
import no.nav.pensjon.vtp.testmodell.dkif.DkifResponse
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Digital kontaktinformasjon"])
@RequestMapping("/rest/dkif")
class DkifMock {

    @GetMapping(path = ["/v1/personer/kontaktinformasjon"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getIdenter(@RequestHeader("Nav-Personidenter") requestIdenter: List<String>): DkifResponse {
        return DkifResponse(kontaktinfo = emptyMap(), feil = emptyMap())
    }

    @GetMapping("/ping")
    fun ping() = ok("pong")
}
