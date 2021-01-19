package no.nav.pensjon.vtp.mocks.navansatt

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Api(tags = ["NavansattRestMock"])
@RequestMapping("/rest/navansatt")
class NavAnsattRestMock(
    private val ansatteIndeks: AnsatteIndeks,
    private val enheterIndeks: EnheterIndeks
) {

    @ApiOperation(value = "ping", notes = "Ping-operasjon")
    @GetMapping(
        value = ["/ping"],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun ping(): ResponseEntity<String> = ResponseEntity.of(Optional.of("OK"))

    @ApiOperation(value = "ping-authenticated", notes = "Ping-operasjon (med autentisering)")
    @GetMapping(
        value = ["/ping-authenticated"],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun pingAuthenticated(
        @RequestHeader("Authorization", required = false) authorization: String?
    ): ResponseEntity<String> =
        if (authorization != null) {
            ResponseEntity.of(Optional.of("OK, auth = $authorization"))
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Forbidden: this endpoint requires an Authorization header to be set.")
        }

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

    data class Fagomrade(
        val kode: String
    )

    @GetMapping(
        value = ["/navansatt/{ident}/fagomrader"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiOperation(value = "hentNavAnsattFagomrader", notes = "Hent fagområder for en NAV-ansatt")
    fun hentNavAnsattFagomrader(@PathVariable("ident") ident: String): ResponseEntity<List<Fagomrade>> {
        return ResponseEntity.ok(
            listOf(
                Fagomrade("PEPPERKAKE"),
                Fagomrade("PEN"),
                Fagomrade("UFO")
            )
        )
    }

    data class NAVEnhetResult(
        val id: String,
        val navn: String
    )

    @GetMapping(
        value = ["/navansatt/{ident}/enheter"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiOperation(value = "hentNavAnsattEnheter", notes = "Hent enheter for en NAV-ansatt")
    fun hentNavAnsattEnheter(@PathVariable("ident") ident: String): ResponseEntity<List<NAVEnhetResult>> {
        val ansatt = ansatteIndeks.findByCn(ident)
            ?: throw HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg("NAV-ansatt '$ident' ikke funnet.")

        return ResponseEntity.ok(
            enheterIndeks.findByEnhetIdIn(ansatt.enheter).map {
                NAVEnhetResult(
                    id = it.enhetId.toString(),
                    navn = it.navn
                )
            }
        )
    }

    @GetMapping(
        value = ["/enhet/{enhetId}/navansatte"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiOperation(value = "hentEnhetNavAnsatte", notes = "Hent NAV-ansatte for en enhet")
    fun hentEnhetNavAnsatte(@PathVariable("enhetId") enhetId: String): ResponseEntity<List<UserResult>> {
        return ResponseEntity.ok(
            ansatteIndeks.findByEnhetsId(enhetId.toLong()).map {
                UserResult(
                    ident = it.cn,
                    navn = it.displayName,
                    fornavn = it.givenname,
                    etternavn = it.sn,
                    epost = it.email
                )
            }
        )
    }
}
