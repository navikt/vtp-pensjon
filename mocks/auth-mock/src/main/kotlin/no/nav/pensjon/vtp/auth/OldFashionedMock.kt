package no.nav.pensjon.vtp.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jwt.JWTParser
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["OldFashioned"])
@RequestMapping("/rest/old-fashioned")
class OldFashionedMock(
    private val ansatteIndeks: AnsatteIndeks,
    private val jsonWebKeySupport: JsonWebKeySupport,
    @Value("\${ISSO_OAUTH2_ISSUER}") val issuer: String
) {
    private val logger = getLogger(OldFashionedMock::class.java)

    data class OldFashionedTokenResponse(
        @JsonProperty("access_token")
        val accessToken: String
    )
    @PostMapping(value = ["/token"], consumes = [APPLICATION_FORM_URLENCODED_VALUE], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "/token", notes = "Veksle inn Azure AD-token til OpenAM-token")
    fun exchange(
        @RequestParam("subject_token") subjectToken: String
    ): ResponseEntity<Any> {
        val email = JWTParser.parse(subjectToken).jwtClaimsSet.getStringClaim("preferred_username")
        val ansatt = ansatteIndeks.findByEmail(email)
        if (ansatt == null) {
            return badRequest().body("Ansatt with e-mail $email was not found in VTP.")
        }
        val token = OidcTokenGenerator(
            jsonWebKeySupport = jsonWebKeySupport,
            subject = ansatt.cn,
            nonce = null,
            issuer = issuer
        ).create()
        return ok(OldFashionedTokenResponse(accessToken = token))
    }
}
