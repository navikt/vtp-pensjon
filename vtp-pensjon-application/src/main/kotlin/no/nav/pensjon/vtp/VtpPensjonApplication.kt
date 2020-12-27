package no.nav.pensjon.vtp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.lang.System.setProperty

@SpringBootApplication
class VtpPensjonApplication

fun main(args: Array<String>) {
    setProperty("scenarios.dir", "./model/scenarios")
    runApplication<VtpPensjonApplication>(*args)
}