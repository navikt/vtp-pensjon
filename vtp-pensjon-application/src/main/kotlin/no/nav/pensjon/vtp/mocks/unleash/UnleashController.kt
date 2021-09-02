package no.nav.pensjon.vtp.mocks.unleash

import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/rest/unleash")
class UnleashController(
    private val featureRepository: FeatureRepository
) {
    @GetMapping("/api/client/features")
    fun features() = FeatureResource(
        features = featureRepository.findAll()
    )

    @PutMapping("/api/client/features")
    fun updateFeature(@RequestBody feature: Feature) {
        featureRepository.save(feature)
    }

    @DeleteMapping("/api/client/features/{name}")
    fun deleteFeature(@PathVariable name: String) {
        featureRepository.deleteByName(name)
    }

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
}
