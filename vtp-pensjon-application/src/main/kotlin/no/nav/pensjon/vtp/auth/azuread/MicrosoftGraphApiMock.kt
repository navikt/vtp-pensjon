package no.nav.pensjon.vtp.auth.azuread

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.Api
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.lang.RuntimeException

@RestController
@Api(tags = ["AzureAd"])
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
            context = "https://graph.microsoft.com/v1.0/\$metadata#users($select)/\$entity",
            onPremisesSamAccountName = ident,
            memberOf = ansatt.groups.map { Group(it) }
        )
    }

    private fun getAnsatt(auth: String): Pair<String, NAVAnsatt> {
        if (!auth.startsWith("Bearer access:")) {
            throw ResponseStatusException(FORBIDDEN, "Bad mock access token; must be on format Bearer access:<userid>")
        } else {
            val ident = auth.substring(14).split(";".toRegex()).toTypedArray()[0]
            val ansatt = ansatteIndeks.findByCn(ident)
                ?: throw RuntimeException("Ansatt med ident $ident ikke funnet i VTP.")
            return Pair(ident, ansatt)
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
        @JsonProperty("@odata.context") var context: String,
        var onPremisesSamAccountName: String,
        var memberOf: List<Group>
    )

    data class Group(var displayName: String)
}
