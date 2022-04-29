package no.nav.pensjon.vtp.client.unleash

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

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
                    RequestBody.create(
                        MediaType.parse("application/json"),
                        objectMapper.writeValueAsString(
                            UnleashFeature(
                                name = name,
                                enabled = enabled
                            )
                        )
                    )
                )
                .build()
        )
        .execute()
        .also { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Failed to ${if (enabled) "enable" else "disable"} toggle with status: ${response.code()}")
            }
        }.isSuccessful
}
