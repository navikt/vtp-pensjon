package no.nav.pensjon.vtp.mocks.unleash

import org.springframework.data.annotation.Id
import java.time.LocalDate.now

data class Feature(
    @Id
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
