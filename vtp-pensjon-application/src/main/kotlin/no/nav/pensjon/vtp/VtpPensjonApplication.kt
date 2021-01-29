package no.nav.pensjon.vtp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VtpPensjonApplication

fun main(args: Array<String>) {
    runApplication<VtpPensjonApplication>(*args)
}
