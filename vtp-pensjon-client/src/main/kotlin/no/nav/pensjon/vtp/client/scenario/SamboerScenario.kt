package no.nav.pensjon.vtp.client.scenario

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class SamboerScenario(
    private val vtpPensjonUrl: String,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
) {
    private val objectMapper = jsonMapper {
        addModule(kotlinModule())
    }

    fun initScenario(key: String) = okHttpClient
        .newCall(
            Request.Builder()
                .url("$vtpPensjonUrl/api/testscenarios/${key}")
                .post(RequestBody.create(null, ""))
                .build()
        )
        .execute()
        .also { response ->
            if (!response.isSuccessful) {
                throw RuntimeException("Failed with scenario key: ${key} and response status: ${response.code()}")
            }
        }.run { objectMapper.readValue(body()?.string(), SamboerScenarioDto::class.java) }
}
