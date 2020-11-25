package no.nav.pensjon.vtp.snitch

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/snitch")
class SnitchController(
        private val requestResponseRepository: RequestResponseRepository
) {
    @GetMapping
    fun findAll() = requestResponseRepository.findAll()
}
