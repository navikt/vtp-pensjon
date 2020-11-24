package no.nav.pensjon.vtp.auth

import io.swagger.annotations.Api
import org.apache.commons.codec.binary.Base64.encodeBase64String
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.StringWriter
import java.time.LocalDateTime
import java.time.LocalDateTime.MAX
import java.time.ZoneId.systemDefault
import java.time.ZonedDateTime
import java.time.ZonedDateTime.of
import java.util.Base64.getUrlEncoder
import javax.xml.bind.JAXB

@RestController
@Api(tags = ["Security Token Service"])
@RequestMapping("/rest/v1/sts")
class STSRestTjeneste(private val jsonWebKeySupport: JsonWebKeySupport, private val generator: STSIssueResponseGenerator, private val samlTokenGenerator: SamlTokenGenerator, @Value("\${ISSO_OAUTH2_ISSUER}") private val issuer: String) {
    @PostMapping(value = ["/token/exchange"], produces = [MediaType.APPLICATION_JSON_VALUE])
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
    @GetMapping(value = ["/token"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun dummyToken(
            @RequestParam grant_type: String?,
            @RequestParam scope: String?
    ): UserTokenResponse {
        return UserTokenResponse(
                access_token = OidcTokenGenerator(
                        jsonWebKeySupport = jsonWebKeySupport,
                        subject = "dummyBruker",
                        nonce = "",
                        issuer = issuer
                ).create(),
                expires_in = 600000 * 1000L,
                token_type = "Bearer"
        )
    }

    @GetMapping(value = ["/samltoken"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(Exception::class)
    fun dummyToken(): ResponseEntity<SAMLResponse> {
        val samlToken = samlTokenGenerator.issueToken("CN=InternBruker,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local")

        return ResponseEntity
                .status(OK)
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(SAMLResponse(
                        access_token = encodeBase64String(samlToken.toByteArray()),
                        decodedToken = samlToken,
                        token_type = "Bearer",
                        issued_token_type = "urn:ietf:params:oauth:token-type:saml2",
                        expires_in = of(MAX, systemDefault())
                ))
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
            val access_token: String? = null,
            val expires_in: Long? = null,
            val token_type: String? = null
    ) {
        /**
         *
         * @param expirationLeeway the amount of seconds to be subtracted from the expirationTime to avoid returning false positives
         * @return `true` if "now" is after the expirationtime(minus leeway), else returns `false`
         */
        @Suppress("unused")
        fun isExpired(expirationLeeway: Long): Boolean {
            return LocalDateTime.now().isAfter(LocalDateTime.now().plusSeconds(expires_in!!).minusSeconds(expirationLeeway))
        }
    }
}