package no.nav.pensjon.vtp.mocks.tp

import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoTjenestepensjonSimulering
import no.nav.lib.sto.sam.asbo.ASBOStoEndringsInfo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Tjenestepensjon(
    @Id
    val pid: String,
    var endringsInfo: ASBOStoEndringsInfo? = null,
    val forhold: Set<Forhold> = emptySet(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tjenestepensjon

        if (pid != other.pid) return false

        return true
    }

    override fun hashCode(): Int {
        return pid.hashCode()
    }
}

data class Forhold(
    val forholdId: String?,
    var tssEksternId: String,
    var navn: String? = null,
    var tpnr: String? = null,
    var harUtlandPensjon: Boolean? = null,
    var samtykkeSimuleringKode: String? = null,
    var samtykkeDato: Calendar? = null,
    var harSimulering: Boolean? = null,
    var tjenestepensjonSimulering: ASBOStoTjenestepensjonSimulering? = null,
    var endringsInfo: ASBOStoEndringsInfo? = null,
    val ytelser: Set<Ytelse> = emptySet(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Forhold

        if (forholdId != other.forholdId) return false

        return true
    }

    override fun hashCode(): Int {
        return forholdId?.hashCode() ?: 0
    }
}

data class Ytelse(
    val ytelseId: String,
    val innmeldtFom: Calendar,
    var ytelseKode: String? = null,
    var ytelseBeskrivelse: String? = null,
    var iverksattFom: Calendar? = null,
    var iverksattTom: Calendar? = null,
    var endringsInfo: ASBOStoEndringsInfo? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ytelse

        if (ytelseId != other.ytelseId) return false

        return true
    }

    override fun hashCode(): Int {
        return ytelseId.hashCode()
    }
}
