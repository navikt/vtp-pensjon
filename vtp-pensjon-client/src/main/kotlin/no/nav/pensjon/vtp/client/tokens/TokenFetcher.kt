package no.nav.pensjon.vtp.client.tokens

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.nimbusds.jwt.JWTParser.parse
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import okhttp3.HttpUrl.get as url
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
    ): TokenMeta = okHttpClient
        .newCall(
            request()
                .post(createBody(null, ""))
                .url(
                    url("$vtpPensjonUrl/rest/maskinporten/mock_access_token").newBuilder()
                        .addQueryParameter("consumer", consumer)
                        .addQueryParameter("scope", scope)
                        .addQueryParameter("issuer", issuer)
                        .build()
                )
                .build()
        )
        .execute().run {
            if (isSuccessful) {
                TokenMeta(
                    token = (body()
                        ?.string()
                        ?: throw RuntimeException("Response from VTP was empty")),
                    username = consumer,
                )
            } else {
                throw RuntimeException("Failed to fetch token status=${code()} body=${body()}")
            }
        }

    fun fetchAzureAdToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ): TokenMeta = okHttpClient
            .newCall(
                request()
                    .postJson(
                        AnsattRequest(
                            groups = groups,
                            units = units
                        )
                    )
                    .url(
                        url("$vtpPensjonUrl/rest/AzureAd/vtp-pensjon/v2.0/ansatt").newBuilder()
                            .addQueryParameter("issuer", issuer)
                            .build()
                    )
                    .header(
                        "Authorization",
                        "Basic ${Base64.getEncoder().encodeToString(("$clientId:dummy").toByteArray())}"
                    )
                    .build()
            )
            .execute().run {
                if (isSuccessful) {
                    val foo = objectMapper.readValue(
                        body()?.string() ?: throw RuntimeException("Response from VTP was empty"),
                        OpenAmTokenResponse::class.java
                    )
                    val jwt = parse(foo.id_token)

                    TokenMeta(
                        token = foo.id_token,
                        username = jwt.jwtClaimsSet.getStringClaim("NAVident"),
                    )
                } else {
                    throw RuntimeException("Failed to fetch token status=${code()} body=${body()?.string()}")
                }
            }

    fun fetchIssoToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ): TokenMeta = okHttpClient
        .newCall(
            request()
                .postJson(
                    AnsattRequest(
                        groups = groups,
                        units = units
                    )
                )
                .url(
                    url("$vtpPensjonUrl/rest/isso/oauth2/ansatt").newBuilder()
                        .addQueryParameter("issuer", issuer)
                        .build()
                )
                .header(
                    "Authorization",
                    "Basic ${Base64.getEncoder().encodeToString(("$clientId:dummy").toByteArray())}"
                )
                .build()
        )
        .execute().run {
            if (isSuccessful) {
                val foo = objectMapper.readValue(
                    body()?.string() ?: throw RuntimeException("Response from VTP was empty"),
                    OpenAmTokenResponse::class.java
                )
                val jwt = parse(foo.id_token)

                TokenMeta(
                    token = foo.id_token,
                    username = jwt.jwtClaimsSet.getStringClaim("NAVident"),
                )
            } else {
                throw RuntimeException("Failed to fetch token status=${code()} body=${body()?.string()}")
            }
        }

    fun fetchStsToken(
        issuer: String,
        user: String,
    ): TokenMeta {
        val request = request()
            .url(
                url("$vtpPensjonUrl/rest/v1/sts/token?grant_type=client_credentials&scope=openid").newBuilder()
                    .addQueryParameter("issuer", issuer)
                    .build()
            )
            .header("Authorization", "Basic ${Base64.getEncoder().encodeToString(("$user:dummy").toByteArray())}")
            .build()

        val body = (okHttpClient.newCall(request).execute().body()
            ?.string()
            ?: throw RuntimeException("Response from VTP was empty"))

        return TokenMeta(
            token = objectMapper.readValue(body, StsTokenResponse::class.java).access_token,
            username = user
        )
    }

    data class AnsattRequest(
        val groups: List<String>,
        val units: List<String>,
    )

    data class OpenAmTokenResponse(
        val id_token: String,
        val refresh_token: String,
        val access_token: String,
        val expires_in: Int,
        val token_type: String
    )

    data class StsTokenResponse(
        val access_token: String,
        val token_type: String,
        val expires_in: Int?,
        val refresh_token: String?,
        val scope: String?
    )

    private fun Request.Builder.postJson(any: Any) = this.post(
        createBody(
            mediaType("application/json"),
            objectMapper.writeValueAsString(any),
        )
    )
}
