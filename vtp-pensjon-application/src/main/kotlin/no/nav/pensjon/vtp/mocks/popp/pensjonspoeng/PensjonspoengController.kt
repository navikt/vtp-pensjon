package no.nav.pensjon.vtp.mocks.popp.pensjonspoeng

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/popp/api/pensjonspoeng")
class PensjonspoengController {
    @GetMapping
    fun getPensjonspoeng(@RequestHeader("fnr") pid: String) = HentPensjonspoengListeResponse()

    @GetMapping("/{fnr}")
    fun getPensjonspoengWithPath(@PathVariable("fnr") pid: String) = HentPensjonspoengListeResponse()
}
