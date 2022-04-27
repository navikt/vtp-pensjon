package no.nav.pensjon.vtp.client

import no.nav.pensjon.vtp.client.tokens.JWT
import no.nav.pensjon.vtp.client.tokens.TokenFetcher
import no.nav.pensjon.vtp.client.tokens.TokenMeta

class VtpPensjonClient(
    baseUrl: String,
    private val azureAdClientId: String? = null,
    private val azureAdIssuer: String? = null,
    private val issoIssuer: String? = null,
    private val maskinportenIssuer: String? = null,
    private val stsIssuer: String? = null,
) {
    private val tokenFetcher = TokenFetcher(
        vtpPensjonUrl = baseUrl,
    )

    fun azureAdOboToken(groups: List<String>, issuer: String? = null, audience: String? = null): TokenMeta =
        tokenFetcher.fetchAzureAdToken(
            issuer = issuer
                ?: azureAdIssuer
                ?: throw IllegalStateException("Must supply a issuer or define azureAdClientId"),
            clientId = audience
                ?: azureAdClientId
                ?: throw IllegalStateException("Must supply a audience or define azureAdClientId"),
            groups = groups,
            units = emptyList()
        ).let {
            TokenMeta(
                tokenResponse = it,
                username = JWT.decode(it.idToken ?: throw RuntimeException("Missing id_token from AzureAD OBO call")).getStringClaim("NAVident"),
            )
        }

    fun issoToken(clientId: String, groups: List<String>, issuer: String? = null): TokenMeta =
        tokenFetcher.fetchIssoToken(
            issuer = issuer
                ?: issoIssuer
                ?: throw IllegalStateException("Must supply a issuer or define issoIssuer"),
            clientId = clientId,
            groups = groups,
            units = emptyList()
        ).let {
            TokenMeta(
                tokenResponse = it,
                username = JWT.decode(it.idToken ?: throw RuntimeException("Missing id_token from isso call")).subject,
            )
        }

    fun maskinportenToken(consumer: String, scope: String, issuer: String? = null) = TokenMeta(
        tokenResponse = tokenFetcher.fetchMaskinportenToken(
            issuer = issuer
                ?: maskinportenIssuer
                ?: throw IllegalStateException("Must supply a issuer or define maskinportenIssuer"),
            consumer = consumer,
            scope = scope
        ))

    fun stsToken(user: String, issuer: String? = null): TokenMeta = tokenFetcher.fetchStsToken(
        issuer = issuer
            ?: stsIssuer
            ?: throw IllegalStateException("Must supply a issuer or define stsIssuer"),
        user = user
    ).let {
        TokenMeta(
            tokenResponse = it,
            username = user,
        )
    }
}
