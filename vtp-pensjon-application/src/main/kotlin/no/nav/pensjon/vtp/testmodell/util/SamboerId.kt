package no.nav.pensjon.vtp.testmodell.util

class SamboerId {
    companion object {
        private var samboerId = 1
        fun nextId() = samboerId++.toString()
    }
}