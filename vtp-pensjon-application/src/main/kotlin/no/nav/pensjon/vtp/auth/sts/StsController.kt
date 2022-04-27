package no.nav.pensjon.vtp.auth.sts

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import no.nav.pensjon.vtp.auth.OidcTokenGenerator
import no.nav.pensjon.vtp.auth.getUser
import no.nav.pensjon.vtp.util.asResponseEntity
import no.nav.pensjon.vtp.util.withoutQueryParameters
import org.apache.commons.codec.binary.Base64.encodeBase64String
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType
import org.jose4j.jwt.NumericDate.now
import org.springframework.beans.factory.annotation.Value
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import java.io.StringWriter
import java.util.Base64.getUrlEncoder
import javax.xml.bind.JAXB.marshal

@RestController
@Tag(name = "Security Token Service")
@RequestMapping("/rest/v1/sts")
class StsController(
    private val oidcTokenGenerator: OidcTokenGenerator,
    private val generator: STSIssueResponseGenerator,
    private val samlTokenGenerator: SamlTokenGenerator,
    @Value("\${STS_OAUTH2_ISSUE}") private val issuer: String
) {
    @PostMapping(value = ["/token/exchange"])
    fun dummySaml(
        @RequestParam grant_type: String,
        @RequestParam subject_token_type: String,
        @RequestParam subject_token: String
    ) = SAMLResponse(
        access_token = asBase64(
            xmlMarshal(
                generator.buildRequestSecurityTokenResponseType(
                    "urn:oasis:names:tc:SAML:2.0:assertion"
                )
            )
        ),
        token_type = "Bearer",
        issued_token_type = subject_token_type,
        expires_in = 3600L
    ).asResponseEntity()

    @RequestMapping(value = ["/token"], method = [GET, POST])
    fun dummyToken(
        @RequestHeader(AUTHORIZATION) authorization: String?,
        @RequestParam grant_type: String?,
        @RequestParam scope: String?,
        @RequestParam("issuer") requestedIssuer: String?,
    ) = getUser(authorization)
        ?.let { user ->
            val expiresInSeconds = 3600L * 6L

            ok(
                UserTokenResponse(
                    access_token = oidcTokenGenerator.oidcToken(
                        subject = user,
                        aud = listOf(
                            user,
                            "vtp-pensjon"
                        ),
                        issuer = requestedIssuer ?: this.issuer,
                        expiration = now().apply { addSeconds(expiresInSeconds) },
                        additionalClaims = mapOf(
                            "azp" to user
                        )
                    ),
                    expires_in = expiresInSeconds,
                    token_type = "Bearer"
                )
            )
        }
        ?: status(UNAUTHORIZED)
            .body(
                ErrorResponse(
                    error = "invalid_client",
                    error_description = "Unauthorised: Full authentication is required to access this resource"
                )
            )

    @GetMapping(value = ["/samltoken"])
    fun dummyToken() = status(OK)
        .header("Cache-Control", "no-store")
        .header("Pragma", "no-cache")
        .body(
            SAMLResponse(
                access_token = encodeBase64String(
                    samlTokenGenerator.issueToken("CN=InternBruker,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local")
                        .toByteArray()
                ),
                token_type = "Bearer",
                issued_token_type = "urn:ietf:params:oauth:token-type:saml2",
                expires_in = 3600L
            )
        )

    @GetMapping("/jwks")
    fun jwks() = JsonWebKeySupport.Keys(oidcTokenGenerator.jwks()).asResponseEntity()

    @GetMapping("/.well-known/openid-configuration")
    fun wellKnown() = WellKnown(
        issuer = issuer,
        token_endpoint = linkTo<StsController> { dummyToken(null, null, null, null) }.toUri().withoutQueryParameters(),
        exchange_token_endpoint = linkTo<StsController> { dummySaml("dummy", "dummy", "dummy") }.toUri().withoutQueryParameters(),
        jwks_uri = linkTo<StsController> { jwks() }.toUri().withoutQueryParameters(),
    )

    data class SAMLResponse(
        val access_token: String,
        val issued_token_type: String,
        val token_type: String,
        val expires_in: Long
    )

    data class UserTokenResponse(
        val access_token: String,
        val expires_in: Long,
        val token_type: String
    )

    data class ErrorResponse(
        val error: String,
        val error_description: String
    )

    companion object {
        private fun xmlMarshal(buildRequestSecurityTokenResponseType: RequestSecurityTokenResponseType) =
            StringWriter().also { marshal(buildRequestSecurityTokenResponseType, it) }.toString().toByteArray()

        private fun asBase64(data: ByteArray) = getUrlEncoder().withoutPadding().encodeToString(data)
    }
}
