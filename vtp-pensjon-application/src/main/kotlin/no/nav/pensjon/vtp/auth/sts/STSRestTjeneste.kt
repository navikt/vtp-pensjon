package no.nav.pensjon.vtp.auth.sts

import io.swagger.annotations.Api
import no.nav.pensjon.vtp.auth.OidcTokenGenerator
import no.nav.pensjon.vtp.auth.getUser
import org.apache.commons.codec.binary.Base64.encodeBase64String
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType
import org.jose4j.jwt.NumericDate.now
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import java.io.StringWriter
import java.time.LocalDateTime.MAX
import java.time.ZoneId.systemDefault
import java.time.ZonedDateTime
import java.time.ZonedDateTime.of
import java.util.Base64.getUrlEncoder
import javax.xml.bind.JAXB

@RestController
@Api(tags = ["Security Token Service"])
@RequestMapping("/rest/v1/sts")
class STSRestTjeneste(
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
    ): SAMLResponse {
        val xmlString = xmlMarshal(generator.buildRequestSecurityTokenResponseType("urn:oasis:names:tc:SAML:2.0:assertion"))

        return SAMLResponse(
            access_token = getUrlEncoder().withoutPadding().encodeToString(xmlString.toByteArray()),
            decodedToken = xmlString,
            token_type = "Bearer",
            issued_token_type = subject_token_type,
            expires_in = of(MAX, systemDefault())
        )
    }

    @GetMapping(value = ["/token"])
    fun dummyToken(
        @RequestHeader(AUTHORIZATION) authorization: String?,
        @RequestParam grant_type: String?,
        @RequestParam scope: String?
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
                        issuer = issuer,
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
    fun dummyToken(): ResponseEntity<SAMLResponse> {
        val samlToken =
            samlTokenGenerator.issueToken("CN=InternBruker,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local")

        return status(OK)
            .header("Cache-Control", "no-store")
            .header("Pragma", "no-cache")
            .body(
                SAMLResponse(
                    access_token = encodeBase64String(samlToken.toByteArray()),
                    decodedToken = samlToken,
                    token_type = "Bearer",
                    issued_token_type = "urn:ietf:params:oauth:token-type:saml2",
                    expires_in = of(MAX, systemDefault())
                )
            )
    }

    private fun xmlMarshal(buildRequestSecurityTokenResponseType: RequestSecurityTokenResponseType?): String {
        val sw = StringWriter()
        JAXB.marshal(buildRequestSecurityTokenResponseType, sw)
        return sw.toString()
    }

    data class SAMLResponse(
        val access_token: String,
        val issued_token_type: String,
        val token_type: String,
        val decodedToken: String,
        val expires_in: ZonedDateTime
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
}