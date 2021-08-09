package no.nav.pensjon.vtp.mocks.unleash

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate.now
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/rest/unleash")
class UnleashController {
    @GetMapping("/api/client/features")
    fun features() = FeatureResource(
        features = listOf(
            Feature(
                name = "pensjon.tp-ejb-adapter.tp-nais",
                description = "Controls the use of tjenestepensjon on nais / was",
                enabled = true
            )
        )
    )

    @PostMapping("/api/client/metrics")
    fun metrics(response: HttpServletResponse) = with(response) {
        addCookie(
            Cookie(
                "unleash-session",
                "eyJub3dJbkhvdXJzIjo0NDk4OTZ9"
            )
        )
        addCookie(
            Cookie(
                "unleash-session.sig",
                "GkWsWE-O5XwV5rW11CCodmpkn8"
            )
        )
        status = 202
    }

    @PostMapping("/api/client/register")
    fun register(response: HttpServletResponse) = with(response) {
        addCookie(
            Cookie(
                "unleash-session",
                "eyJub3dJbkhvdXJzIjo0NDk4OTZ9"
            )
        )
        addCookie(
            Cookie(
                "unleash-session.sig",
                "GkWsWE-O5XwV5rW11CCodmpkn8"
            )
        )
        status = 202
    }

    data class ClientRegistration(
        val appName: String? = null,
        val instanceId: String? = null,
        val sdkVersion: String? = null,
        val strategies: Set<String>? = null,
        val started: LocalDateTime? = null,
        val interval: Long = 0,
        val environment: String? = null,
    )

    data class FeatureResource(val version: Int = 1, val features: List<Feature>)

    data class Feature(
        val name: String,
        val description: String,
        val enabled: Boolean,
        val strategies: List<Strategy> = listOf(Strategy("default")),
        val variants: String? = null,
        val createdAt: String = now().toString()
    )

    data class Strategy(
        val name: String
    )
}
