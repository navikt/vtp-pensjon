package no.nav.pensjon.vtp.auth

import io.swagger.annotations.Api
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.apache.http.client.utils.URIBuilder
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.lang.JoseException
import org.springframework.http.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.HtmlUtils
import java.net.URI
import java.net.URISyntaxException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.stream.Collectors

@RestController
@Api(tags = ["LoginService"])
@RequestMapping("/rest/loginservice")
class LoginService(
    private val jsonWebKeySupport: JsonWebKeySupport,
    private val personModellRepository: PersonModellRepository
) {

    data class UserSummary(
        val ident: String,
        val firstName: String,
        val lastName: String,
        val redirect: String
    )
    @GetMapping(value = ["/users"])
    fun getUsers(@RequestParam("redirect") redirect: String?): List<UserSummary> =
        personModellRepository.findAll().map {
            val redirectUrl = URIBuilder("/rest/loginservice/login-redirect-with-cookie").apply {
                addParameter("fnr", it.ident)
                redirect?.let {  addParameter("redirect", it) }
            }.toString()
            UserSummary(
                ident = it.ident,
                firstName = it.fornavn,
                lastName = it.etternavn,
                redirect = redirectUrl
            )
        }

    // Deprecated: Use the /#/loginservice/login endpoint instead. Leave this legacy endpoint just for a little while, while consumers update.
    @GetMapping(value = ["/login"])
    fun login(@RequestParam("redirect") redirect: String?): ResponseEntity<String> {
        val rows = personModellRepository.findAll().stream()
            .map { (fnr, _, fornavn, etternavn) ->
                val navn = "$fornavn $etternavn"
                "<a href=\"login-redirect-with-cookie?fnr=$fnr&redirect=" + URLEncoder.encode(
                    redirect,
                    StandardCharsets.UTF_8
                ) + "\">" + navn + "</a> (" + fnr + ")<br>"
            }.collect(Collectors.toList())
        val html = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<title>Velg bruker</title>" +
            "</head>" +
            "<body>" +
            "<div style=\"text-align:center;width:100%;\">" +
            "<h3>Velg bruker:</h3>" +
            java.lang.String.join("", rows) +
            (if (rows.isEmpty()) "Det finnes visst ingen personer i VTP akkurat nå. Prøv å <a href=\"/#/\">laste inn et scenario</a>!" else "") +
            "<form method=\"get\" action=\"login-redirect-with-cookie\">" +
            "<input type=\"hidden\" name=\"redirect\" value=\"" + HtmlUtils.htmlEscape(redirect!!) + "\">" +
            "<input name=\"fnr\" placeholder=\"Fyll inn et annet fødselsnummer\" style=\"width: 200px\"><input type=\"submit\"></form>" +
            "</div>" +
            "</body>" +
            "</html>"
        return ResponseEntity.ok(html)
    }

    @GetMapping(value = ["/login-redirect-with-cookie"])
    @Throws(URISyntaxException::class, JoseException::class)
    fun doLogin(@RequestParam("redirect") redirect: String?, @RequestParam("fnr") fnr: String?): ResponseEntity<*> {
        val now = NumericDate.now()
        val claims = JwtClaims()
        claims.subject = fnr
        claims.issuer = "https://login.microsoftonline.com/d38f25aa-eab8-4c50-9f28-ebf92c1256f2/v2.0/"
        claims.issuedAt = now
        claims.notBefore = now
        claims.setGeneratedJwtId()
        claims.expirationTime = NumericDate.fromSeconds(now.value + 3600 * 6)
        claims.setAudience("0090b6e1-ffcc-4c37-bc21-049f7d1f0fe5")
        claims.setClaim("ver", "1.0")
        claims.setClaim("acr", "Level4")
        claims.setNumericDateClaim("auth_time", now)
        claims.setClaim("nonce", "hardcoded")
        claims.setClaim("at_hash", "unknown")
        val token = jsonWebKeySupport.createRS256Token(claims.toJson()).compactSerialization
        val cookie: HttpCookie = ResponseCookie.from("selvbetjening-idtoken", token)
            .path("/").maxAge(-1L).httpOnly(false).secure(false).build()
        return ResponseEntity
            .status(HttpStatus.TEMPORARY_REDIRECT)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .location(URI(redirect))
            .build<Any>()
    }
}
