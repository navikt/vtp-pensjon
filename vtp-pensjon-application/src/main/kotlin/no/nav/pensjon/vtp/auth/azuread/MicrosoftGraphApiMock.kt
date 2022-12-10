package no.nav.pensjon.vtp.auth.azuread

import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jose.JOSEObject
import com.nimbusds.jwt.JWTClaimsSet
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@Tag(name = "AzureAd")
@RequestMapping("/rest/MicrosoftGraphApi")
class MicrosoftGraphApiMock(private val ansatteIndeks: AnsatteIndeks) {
    @GetMapping(value = ["/oidc/userinfo"], produces = [APPLICATION_JSON_VALUE])
    fun userinfo(@RequestHeader("Authorization") auth: String): UserInfo {
        val (ident, ansatt) = getAnsatt(auth)

        return UserInfo(
            sub = ident,
            name = ansatt.displayName,
            family_name = ansatt.cn,
            given_name = ansatt.givenname,
            picture = "http://example.com/picture.jpg",
            email = ansatt.email
        )
    }

    @GetMapping(value = ["/v1.0/me"], produces = [APPLICATION_JSON_VALUE])
    fun me(@RequestHeader("Authorization") auth: String, @RequestParam(required = false) select: String?): User {
        val (ident, ansatt) = getAnsatt(auth)

        return User(
            id = ident,
            context = "https://graph.microsoft.com/v1.0/\$metadata#users(id,onPremisesSamAccountName)/\$entity",
            onPremisesSamAccountName = ident,
            memberOf = ansatt.groups.map { Group(it) }
        )
    }

    @GetMapping(value = ["/v1.0/users/{id}/memberOf/microsoft.graph.group"], produces = [APPLICATION_JSON_VALUE])
    fun memberOf(@RequestHeader("Authorization") auth: String, @PathVariable id: String): MemberOfResponse {
        val (_, ansatt) = getAnsatt(auth)

        return MemberOfResponse(
            context = "https://graph.microsoft.com/v1.0/\$metadata#groups(displayName)",
            nextLink = null,
            value = ansatt.groups.map { Group(it) }
        )
    }

    private fun getAnsatt(auth: String): Pair<String, NAVAnsatt> {
        if (!auth.startsWith("Bearer ")) {
            throw ResponseStatusException(FORBIDDEN, "Bad mock access token; must be on format Bearer access:<userid>")
        } else {
            val assertion = auth.substring("Bearer ".length)
            val claims = JWTClaimsSet.parse(JOSEObject.parse(assertion).payload.toJSONObject())
            val ansattId: String = claims.subject.split(":")[1]
            val ansatt = ansatteIndeks.findByCn(ansattId)
                ?: throw RuntimeException("Ansatt med ident $ansattId ikke funnet i VTP.")
            return Pair(ansattId, ansatt)
        }
    }

    data class UserInfo(
        val sub: String,
        val name: String,
        val family_name: String,
        val given_name: String,
        val picture: String,
        val email: String
    )

    data class User(
        val id: String,
        @JsonProperty("@odata.context") var context: String,
        var onPremisesSamAccountName: String,
        var memberOf: List<Group>
    )

    data class MemberOfResponse(
        @JsonProperty("@odata.context") val context: String,
        @JsonProperty("@odata.nextLink") val nextLink: String?,
        val value: List<Group>
    )

    data class Group(var displayName: String)
}
