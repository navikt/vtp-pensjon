package no.nav.pensjon.vtp.testmodell.enums

import java.util.*

enum class Kjonn {
    MANN,
    KVINNE;

    companion object {
        private val RANDOM = Random()

        fun randomKjonn(): Kjonn {
            return values()[RANDOM.nextInt(values().size)]
        }
    }
}
