package no.nav.pensjon.vtp.client.unleash

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import okhttp3.OkHttpClient
import okhttp3.Request

internal data class UnleashFeatures(
    @JsonProperty("version")
    val version: Int,
    @JsonProperty("features")
    val features: List<UnleashFeature>,
)

internal class UnleashTestUtil(
    private val vtpPensjonUrl: String,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
) {

    private val objectMapper = jsonMapper {
        addModule(kotlinModule())
    }

    fun isEnabled(name: String) = okHttpClient
        .newCall(
            Request.Builder()
                .url("$vtpPensjonUrl/rest/unleash/api/client/features")
                .get()
                .build()
        )
        .execute()
        .let { objectMapper.readValue(it.body?.string(), UnleashFeatures::class.java)}
        .features
        .filter { it.name == name }
        .takeUnless { it.isEmpty() }?.first()?.enabled ?: false
}
