package no.nav.pensjon.vtp.mocks.kontoregister

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@Tag(name = "Kontoregister")
@RequestMapping("/api/system/v1")
class SokosKontoregisterController {

    @PostMapping("/hent-aktiv-konto")
    fun mockKontoinformasjon(@RequestBody kontoRequest: KontoRequest): Kontoinformasjon {
        return Kontoinformasjon(
            kontoRequest.kontohaver,
            "01234567892",
            null,
            LocalDateTime.parse("2022-01-01"),
            null,
            null,
            kontoRequest.kontohaver,
            null
        )
    }
}