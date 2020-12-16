package no.nav.pensjon.vtp.mocks.enonic

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/enonic")
class EnonicMock {
    @GetMapping(value = ["/**"])
    operator fun get(request: HttpServletRequest): ResponseEntity<*> {
        return ResponseEntity.ok().build<Any>()
    }
}
