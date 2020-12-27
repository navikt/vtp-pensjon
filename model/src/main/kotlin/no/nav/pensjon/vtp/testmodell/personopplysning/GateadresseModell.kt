package no.nav.pensjon.vtp.testmodell.personopplysning

import com.fasterxml.jackson.annotation.JsonTypeName
import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import java.time.LocalDate

@JsonTypeName("gateadresse")
data class GateadresseModell(
    override val fom: LocalDate?,
    override val tom: LocalDate?,
    override val endringstype: Endringstype?,
    override val endringstidspunkt: LocalDate?,
    override val adresseType: AdresseType,
    override val land: Landkode,
    val gatenavn: String?,
    val gatenummer: Int?,
    val husbokstav: String?,
    val husnummer: Int?,
    val postnummer: String?,
    val kommunenummer: String?,
    val tilleggsadresse: String?,
    val tilleggsadresseType: String?
) : AdresseModell {
    override fun clone(fom: LocalDate?, tom: LocalDate?): AdresseModell = copy(fom = fom, tom = tom)
}
