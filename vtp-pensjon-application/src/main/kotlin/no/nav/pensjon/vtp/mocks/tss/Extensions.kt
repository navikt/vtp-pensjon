package no.nav.pensjon.vtp.mocks.tss

import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenAlternativId
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenAvdeling

fun Array<ASBOPenAlternativId>.ofType(type: String) = firstOrNull { it.alternativIdKode == type }?.alternativId

fun Array<ASBOPenAlternativId>.tpNr() = ofType("TPNR")

fun Array<ASBOPenAlternativId>.orgNr() = ofType("ORG")?.let {
    if (it.length == 11 && it.startsWith("00")) it.substring(2) else it
}

fun Array<ASBOPenAvdeling>.tjenestepensjon() = firstOrNull {
    it.avdelingType.startsWith("TP")
} ?: run {
    if (size > 1) {
        throw RuntimeException("Don't know how to extract correct avdeling when a set of $size avdelinger are returned and non are of type TP")
    }
    first()
}
