package no.nav.pensjon.vtp.client.unleash

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UnleashFeature(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("description")
    val description: String = "",
    @JsonProperty("enabled")
    val enabled: Boolean,
    @JsonProperty("strategies")
    val strategies: List<Strategy> = listOf(Strategy("default")),
    @JsonProperty("variants")
    val variants: String? = null,
    @JsonProperty("createdAt")
    val createdAt: String = LocalDate.now().toString()
) {
    data class Strategy(
        @JsonProperty("name")
        val name: String
    )
}
