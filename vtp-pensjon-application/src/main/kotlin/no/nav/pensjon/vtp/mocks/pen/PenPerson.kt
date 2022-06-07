package no.nav.pensjon.vtp.mocks.pen

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@Tag(name = "PenPerson")
@RequestMapping("/rest/pen/api/person")
class PenPerson {

    @GetMapping(path = ["/erPenPerson"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun erPenPerson(@RequestParam(name = "Pid") request: List<String>): ErPenPersonResponse = ErPenPersonResponse()
}

data class ErPenPersonResponse(val fnrListe: List<String> = listOf())
