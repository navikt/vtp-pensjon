package no.nav.pensjon.vtp.mocks.krr

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.krr.DigdirRepository
import no.nav.pensjon.vtp.testmodell.krr.DigitalKontaktinformasjon
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Innhenting av digitale kontaktinformasjon")
@RequestMapping("/rest/krr/")
internal class DigdirKrrProxyMock(
    private val digdirRepository: DigdirRepository
) {

    @GetMapping("/rest/v1/person")
    fun personController(
        @RequestHeader("Nav-Personident") ident: String,
        @RequestHeader("Nav-Call-Id") navCallId: String,
        @RequestHeader("inkluderSikkerDigitalPost") inkluderSikkerDigitalPost: Boolean = false
    ) = digdirRepository.findById(ident)
        ?: DigitalKontaktinformasjon(personident = ident, aktiv = false)

    @GetMapping("/rest/ping")
    fun ping() = ok()
}