package no.nav.pensjon.vtp.mocks.oppgave.rest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.Api
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Mapper Mock"])
@RequestMapping(
    value = ["/rest/oppgave/api/v1/mapper"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class MapperMockImpl {
    private val mapper = arrayListOf(
        Mappe(1L, "0100", "MAPPE-0100-PEN", "PEN"),
        Mappe(2L, "0310", "MAPPE-0310-PEN", "PEN")
    )

    @GetMapping
    fun hentMapper(@RequestHeader httpHeaders: HttpHeaders?) = HentMapperResponse(antallTreffTotalt = mapper.size, mapper = mapper)

    data class Mappe(
        val id: Long,
        val enhetsnr: String,
        val navn: String,
        val tema: String
    )

    data class HentMapperResponse(
        @JsonProperty("antallTreffTotalt")
        val antallTreffTotalt: Int,
        @JsonProperty("mapper")
        val mapper: List<Mappe>
    )
}
