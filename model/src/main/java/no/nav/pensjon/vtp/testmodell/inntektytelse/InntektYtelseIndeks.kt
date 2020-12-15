package no.nav.pensjon.vtp.testmodell.inntektytelse

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InntektYtelseIndeks {
    private val byIdent: MutableMap<String, InntektYtelseModell> = ConcurrentHashMap()

    fun getInntektYtelseModell(ident: String): InntektYtelseModell? {
        return byIdent[ident]
    }

    fun leggTil(ident: String, iy: InntektYtelseModell) {
        byIdent[ident] = iy
    }

    fun entries(): Map<String, InntektYtelseModell> = byIdent

    fun getInntektYtelseModellFraAktørId(aktørId: String): InntektYtelseModell? {
        return getInntektYtelseModell(aktørId.substring(aktørId.length - 11))
    }
}
