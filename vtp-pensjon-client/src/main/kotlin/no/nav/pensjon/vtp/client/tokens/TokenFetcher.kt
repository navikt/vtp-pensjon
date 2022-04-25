package no.nav.pensjon.vtp.client.tokens

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import no.nav.pensjon.vtp.client.support.basicAuth
import no.nav.pensjon.vtp.client.support.url
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.MediaType.parse as mediaType
import okhttp3.Request.Builder as request
import okhttp3.RequestBody.create as createBody

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
    ) = okHttpClient
        .newCall(
            request()
                .post(createBody(null, ""))
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
        .readJsonResponse<AccessTokenResponse, AccessTokenResponse> {
            this
        }

    fun fetchAzureAdToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ) = okHttpClient
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
        .readJsonResponse<IdTokenResponse, TokenMeta<String>> {
            TokenMeta(
                token = id_token,
                username = JWT.decode(id_token).getStringClaim("NAVident")
            )
        }

    fun fetchIssoToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ): TokenMeta<String> = okHttpClient
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
        .readJsonResponse<IdTokenResponse, TokenMeta<String>> {
            TokenMeta(
                token = id_token,
                username = JWT.decode(id_token).subject
            )
        }

    fun fetchStsToken(
        issuer: String,
        user: String,
    ): TokenMeta<String> = okHttpClient
        .newCall(
            request()
                .url(
                    url = "$vtpPensjonUrl/rest/v1/sts/token",
                    queryParameters = mapOf(
                        "issuer" to issuer,
                        "grant_type" to "client_credentials",
                        "scope" to "openid"
                    )
                )
                .basicAuth(username = user, password = "dummy")
                .build()
        )
        .readJsonResponse<AccessTokenResponse, TokenMeta<String>> {
            TokenMeta(
                token = access_token,
                username = user
            )
        }

    data class AnsattRequest(
        val groups: List<String>,
        val units: List<String>,
    )

    private fun Request.Builder.postJson(any: Any) = this.post(
        createBody(
            mediaType("application/json"),
            objectMapper.writeValueAsString(any),
        )
    )

    private inline fun <reified T, R> Call.readJsonResponse(block: T.() -> R): R = execute().run {
        if (isSuccessful) {
            block(readJsonResponse<T>())
        } else {
            throw RuntimeException("Failed to fetch token status=${code()} body=${body()?.string()}")
        }
    }

    private inline fun <reified T> Response.readJsonResponse() = objectMapper.readValue(
        responseBody(),
        T::class.java
    )


    companion object {
        private fun Response.responseBody() = (body()
            ?.string()
            ?: throw RuntimeException("Response from VTP was empty"))

    }
}
