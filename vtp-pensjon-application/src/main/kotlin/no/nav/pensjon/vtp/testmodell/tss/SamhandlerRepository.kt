package no.nav.pensjon.vtp.testmodell.tss

import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandler

class SamhandlerRepository(
    private val samhandlere: List<ASBOPenSamhandler>
) {
    fun findByTssEksternId(tssEksternId: String): ASBOPenSamhandler? =
        samhandlere.firstOrNull { s -> s.avdelinger.any { a -> a.idTSSEkstern == tssEksternId } }

    fun findAll() = samhandlere
}
