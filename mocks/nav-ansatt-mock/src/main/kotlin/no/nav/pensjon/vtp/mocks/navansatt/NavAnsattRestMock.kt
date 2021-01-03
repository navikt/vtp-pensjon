package no.nav.pensjon.vtp.mocks.navansatt

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["NavansattRestMock"])
@RequestMapping("/rest/navansatt")
class NavAnsattRestMock(
    private val ansatteIndeks: AnsatteIndeks
) {

    data class UserResult(
        val ident: String,
        val navn: String,
        val fornavn: String,
        val etternavn: String,
        val epost: String
    )
    @GetMapping(
        value = ["/navansatt/{ident}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiOperation(value = "hentNavAnsatt", notes = "Hent data om en NAV-ansatt")
    fun hentNavAnsatt(@PathVariable("ident") ident: String): ResponseEntity<UserResult> {
        val res: ResponseEntity<UserResult> = ansatteIndeks.findByCn(ident)?.let {
            ResponseEntity.ok(
                UserResult(
                    ident = it.cn,
                    navn = it.displayName,
                    fornavn = it.givenname,
                    etternavn = it.sn,
                    epost = it.email
                )
            )
        } ?: ResponseEntity.notFound().build()
        return res
    }
}
