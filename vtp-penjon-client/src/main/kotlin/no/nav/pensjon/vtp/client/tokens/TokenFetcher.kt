package no.nav.pensjon.vtp.client.tokens

import com.nimbusds.jwt.JWTParser.parse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.util.*

internal class TokenFetcher(
    val vtpPensjonUrl: String = "http://localhost:8060",
    val restTemplate: RestTemplate = RestTemplate(),
) {
    fun fetchMaskinportenToken(
        issuer: String,
        consumer: String,
        scope: String
    ): TokenMeta = TokenMeta(
        restTemplate.postForEntity(
            "$vtpPensjonUrl/rest/maskinporten/mock_access_token?consumer={consumer}&scope={scope}&issuer=${issuer}",
            null,
            String::class.java,
            mapOf(
                "consumer" to consumer,
                "scope" to scope,
            )
        )
            .apply { if (!statusCode.is2xxSuccessful) throw RuntimeException("Failed to fetch token status=${statusCode} body=${body}") }.body
            ?: throw RuntimeException("Response from VTP was empty"), consumer
    )

    fun fetchAzureAdToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ): TokenMeta = restTemplate.postForEntity(
        "$vtpPensjonUrl/rest/AzureAd/vtp-pensjon/v2.0/ansatt?issuer=${issuer}",
        HttpEntity(
            AnsattRequest(
                groups = groups,
                units = units
            ),
            HttpHeaders().apply {
                add(
                    AUTHORIZATION,
                    "Basic ${Base64.getEncoder().encodeToString(("$clientId:dummy").toByteArray())}"
                )
            }),
        OpenAmTokenResponse::class.java,
    ).body?.id_token
        ?.let {
            val jwt = parse(it)
            TokenMeta(it, jwt.jwtClaimsSet.getStringClaim("NAVident"))
        }
        ?: throw RuntimeException("Response from VTP was empty")


    fun fetchIssoToken(
        issuer: String,
        clientId: String,
        groups: List<String>,
        units: List<String>,
    ) = restTemplate.postForEntity(
        "$vtpPensjonUrl/rest/isso/oauth2/ansatt?issuer=${issuer}",
        HttpEntity(
            AnsattRequest(
                groups = groups,
                units = units
            ),
            HttpHeaders().apply {
                add(
                    AUTHORIZATION,
                    "Basic ${Base64.getEncoder().encodeToString(("$clientId:dummy").toByteArray())}"
                )
            }),
        OpenAmTokenResponse::class.java,
    ).body?.id_token
        ?.let {
            TokenMeta(it, parse(it).jwtClaimsSet.subject)
        }
        ?: throw RuntimeException("Response from VTP was empty")

    fun fetchStsToken(
        issuer: String,
        user: String,
    ) = restTemplate.exchange(
        "$vtpPensjonUrl/rest/v1/sts/token?grant_type=client_credentials&scope=openid&issuer=${issuer}",
        HttpMethod.GET,
        HttpEntity<Any>(
            HttpHeaders().apply {
                add(
                    AUTHORIZATION,
                    "Basic ${Base64.getEncoder().encodeToString(("$user:dummy").toByteArray())}"
                )
            }),
        StsTokenResponse::class.java
    ).body
        ?.let {
            TokenMeta(it.access_token, user)
        }
        ?: throw RuntimeException("Response from VTP was empty")

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
}
