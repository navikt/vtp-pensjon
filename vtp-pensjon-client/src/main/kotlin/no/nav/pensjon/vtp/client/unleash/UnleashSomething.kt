package no.nav.pensjon.vtp.client.unleash

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import no.nav.pensjon.vtp.client.support.APPLICATION_JSON
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class UnleashSomething(
    private val vtpPensjonUrl: String,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
) {
    private val objectMapper = jsonMapper {
        addModule(kotlinModule())
    }

    fun toggle(name: String, enabled: Boolean) = okHttpClient
        .newCall(
            Request.Builder()
                .url("$vtpPensjonUrl/rest/unleash/api/client/features")
                .put(
                    UnleashFeature(
                        name = name,
                        enabled = enabled
                    ).asJsonRequestBody()
                )
                .build()
        )
        .execute()
        .also { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Failed to ${if (enabled) "enable" else "disable"} toggle with status: ${response.code}")
            }
        }.isSuccessful

    private fun Any.asJsonRequestBody() =
        objectMapper.writeValueAsString(this).toRequestBody(APPLICATION_JSON)
}
