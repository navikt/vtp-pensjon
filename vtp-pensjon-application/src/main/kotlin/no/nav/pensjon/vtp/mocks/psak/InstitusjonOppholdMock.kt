package no.nav.pensjon.vtp.mocks.psak

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "InstitusjonOpphold")
@RequestMapping("/rest/v1/person/institusjonsopphold")
class InstitusjonOppholdMock {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getIdenter(@RequestHeader("Nav-Personident") requestIdent: String?): List<Institusjonsopphold> = emptyList()
}
