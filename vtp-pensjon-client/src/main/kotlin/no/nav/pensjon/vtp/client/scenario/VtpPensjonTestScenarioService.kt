package no.nav.pensjon.vtp.client.scenario

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import no.nav.pensjon.vtp.client.support.APPLICATION_JSON
import no.nav.pensjon.vtp.common.testscenario.VtpPensjonTestScenario
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class VtpPensjonTestScenarioService(
    private val vtpPensjonUrl: String,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
    private val objectMapper: JsonMapper = jsonMapper {
        addModule(kotlinModule())
    }
) {
    fun createScenario(vtpPensjonTestScenario: VtpPensjonTestScenario): VtpPensjonTestScenario = okHttpClient
        .newCall(
            Request.Builder()
                .url("$vtpPensjonUrl/api/vtp-pensjon/testscenario")
                .postJson(vtpPensjonTestScenario)
                .build()
        )
        .readSuccessfulJsonResponse()

    private fun Request.Builder.postJson(any: Any) = this.post(
        objectMapper.writeValueAsString(any)
            .toRequestBody(APPLICATION_JSON)
    )

    private inline fun <reified T> Call.readSuccessfulJsonResponse(): T = execute().run {
        if (isSuccessful) {
            val body = body
                ?.string()
                ?: throw RuntimeException("Response from VTP was empty")

            return objectMapper.readValue(body, T::class.java)
        } else {
            throw RuntimeException("Failed to fetch token status=$code body=${body?.string()}")
        }
    }

}
