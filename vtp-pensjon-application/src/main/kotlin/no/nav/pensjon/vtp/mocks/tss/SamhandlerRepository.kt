package no.nav.pensjon.vtp.mocks.tss

import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandler

class SamhandlerRepository(
    private val samhandlere: List<ASBOPenSamhandler>
) {
    fun findByTssEksternId(tssEksternId: String): ASBOPenSamhandler? =
        samhandlere.firstOrNull { s -> s.avdelinger.any { a -> a.idTSSEkstern == tssEksternId } }

    fun findByTpNr(tpNr: String): ASBOPenSamhandler? =
        samhandlere.firstOrNull { s -> s.alternativeIder.any { a -> a.alternativIdKode == "TPNR" && a.alternativId == tpNr } }

    fun findAll() = samhandlere
}
