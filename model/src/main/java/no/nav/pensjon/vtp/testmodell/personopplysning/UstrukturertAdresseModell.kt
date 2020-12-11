package no.nav.pensjon.vtp.testmodell.personopplysning

import com.fasterxml.jackson.annotation.JsonTypeName
import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import java.time.LocalDate

@JsonTypeName("ustrukturert")
data class UstrukturertAdresseModell(
    override val fom: LocalDate?,
    override val tom: LocalDate?,
    override val endringstype: Endringstype?,
    override val endringstidspunkt: LocalDate?,
    override val adresseType: AdresseType,
    override val land: Landkode,
    val adresseLinje1: String?,
    val adresseLinje2: String?,
    val adresseLinje3: String?,
    val adresseLinje4: String?,
    val postNr: String?,
    val poststed: String?
) : AdresseModell {
    override fun clone(fom: LocalDate?, tom: LocalDate?): AdresseModell = copy(fom = fom, tom = tom)
}
