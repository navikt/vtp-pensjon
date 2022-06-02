package no.nav.pensjon.vtp.client.tokens

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import no.nav.pensjon.vtp.client.support.APPLICATION_JSON
import no.nav.pensjon.vtp.client.support.basicAuth
import no.nav.pensjon.vtp.client.support.url
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Request.Builder as request

internal class TokenFetcher(
    private val vtpPensjonUrl: String,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
) {
    private val objectMapper = jsonMapper {
        addModule(kotlinModule())
    }

    fun fetchMaskinportenToken(
        issuer: String,
        consumer: String,
        scope: String,
    ): AccessTokenResponse = okHttpClient
        .newCall(
            request()
                .post("".toRequestBody(null))
                .url(
                    url = "$vtpPensjonUrl/rest/maskinporten/access_token",
                    queryParameters = mapOf(
                        "consumer" to consumer,
                        "scope" to scope,
                        "issuer" to issuer
                    )
                )
                .build()
        )
        .readSuccessfulJsonResponse()

    fun fetchAzureAdOboToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ): AccessTokenResponse = okHttpClient
        .newCall(
            request()
                .postJson(
                    AnsattRequest(
                        groups = groups,
                        units = units
                    )
                )
                .url(
                    url = "$vtpPensjonUrl/rest/AzureAd/vtp-pensjon/v2.0/ansatt",
                    queryParameters = mapOf("issuer" to issuer),
                )
                .basicAuth(username = clientId, password = "dummy")
                .build()
        )
        .readSuccessfulJsonResponse()

    fun fetchAzureAdCcToken(audience: String): AccessTokenResponse = okHttpClient
        .newCall(
            request()
                .url("$vtpPensjonUrl/rest/AzureAd/vtp-pensjon/oauth2/v2.0/token")
                .post(
                    FormBody.Builder()
                        .add("client_id", "hmm")
                        .add("grant_type", "client_credentials")
                        .add("scope", audience)
                        .build()
                )
                .build()
        )
        .readSuccessfulJsonResponse()

    fun fetchIssoToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ): AccessTokenResponse = okHttpClient
        .newCall(
            request()
                .postJson(
                    AnsattRequest(
                        groups = groups,
                        units = units
                    )
                )
                .url(
                    url = "$vtpPensjonUrl/rest/isso/oauth2/ansatt",
                    queryParameters = mapOf("issuer" to issuer)
                )
                .basicAuth(username = clientId, password = "dummy")
                .build()
        )
        .readSuccessfulJsonResponse()

    fun fetchStsToken(
        issuer: String,
        user: String,
        scope: String = "openid",
        audience: List<String>? = null,
    ): AccessTokenResponse = okHttpClient
        .newCall(
            request()
                .url(
                    url = "$vtpPensjonUrl/rest/v1/sts/token",
                    queryParameters = mapOf(
                        "issuer" to issuer,
                        "grant_type" to "client_credentials",
                        "scope" to scope
                    ) + (audience?.let { mapOf("audience" to audience.joinToString(",")) } ?: emptyMap())
                )
                .basicAuth(username = user, password = "dummy")
                .build()
        )
        .readSuccessfulJsonResponse()

    fun fetchTokenXToken(
            clientAssertion: String,
            subjectToken: String,
            audience: String
    ): AccessTokenResponse = okHttpClient
        .newCall(
            request()
                .url("$vtpPensjonUrl/rest/tokenx/token")
                .post(
                    FormBody.Builder()
                        .add("client_assertion", clientAssertion)
                        .add("subject_token", subjectToken)
                        .add("audience", audience)
                        .build()
                )
                .build()
        )
        .readSuccessfulJsonResponse()

    data class AnsattRequest(
        val groups: List<String>,
        val units: List<String>,
    )

    private fun Request.Builder.postJson(any: Any) = this.post(
        objectMapper.writeValueAsString(any).toRequestBody(APPLICATION_JSON),
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
