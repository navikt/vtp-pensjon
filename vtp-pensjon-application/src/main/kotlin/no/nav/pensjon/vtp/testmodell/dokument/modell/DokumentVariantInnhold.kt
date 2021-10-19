package no.nav.pensjon.vtp.testmodell.dokument.modell

import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivfiltype
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Variantformat
import java.util.*

data class DokumentVariantInnhold(
    val filType: Arkivfiltype,
    val variantFormat: Variantformat,
    val dokumentInnhold: ByteArray,
    val uuid: String = UUID.randomUUID().toString(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DokumentVariantInnhold

        if (filType != other.filType) return false
        if (variantFormat != other.variantFormat) return false
        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filType.hashCode()
        result = 31 * result + variantFormat.hashCode()
        result = 31 * result + uuid.hashCode()
        return result
    }
}
