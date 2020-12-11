package no.nav.pensjon.vtp.testmodell.personopplysning

import com.fasterxml.jackson.annotation.JsonTypeName
import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import java.time.LocalDate

@JsonTypeName("postboksadresse")
data class PostboksadresseModell(
    override val fom: LocalDate?,
    override val tom: LocalDate?,
    override val endringstype: Endringstype?,
    override val endringstidspunkt: LocalDate?,
    override val adresseType: AdresseType,
    override val land: Landkode,
    val postboksnummer: String?,
    val poststed: String?,
    val postboksanlegg: String?
) : AdresseModell {
    override fun clone(fom: LocalDate?, tom: LocalDate?): AdresseModell = copy(fom = fom, tom = tom)
}
