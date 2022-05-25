package no.nav.pensjon.vtp.mocks.fullmakt

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp

@RestController
@Tag(name = "Fullmakt")
@RequestMapping("/rest/fullmakt/")
class FullmaktController {

    @PostMapping(path = ["/bprof/finnFullmaktMottakere"])
    fun hentFullMakt(@RequestBody body: FinnFullmaktMottakereRequest?) = FinnFullmaktMottakereResponse(emptySet())

    data class FinnFullmaktMottakereResponse(val fullmaktMottakereSet: Set<FullmaktMottaker>)

    data class FullmaktMottaker(
        val aktorIdentGirFullmakt: String,
        val aktorIdentMottarFullmakt: String,
        val kodeAktorTypeGirFullmakt: String,
        val kodeAktorTypeMottarFullmakt: String,
        val kodeFullmaktType: String
    )

    data class FinnFullmaktMottakereRequest(
        val aktorIdentGirFullmaktSet: Set<String>?,
        val kodeFullmaktTypeSet: Set<String>?,
        val datoGyldig: Timestamp?
    )
}
