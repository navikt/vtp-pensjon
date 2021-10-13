package no.nav.pensjon.vtp.testmodell.personopplysning

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import java.time.LocalDate

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(UstrukturertAdresseModell::class),
    JsonSubTypes.Type(GateadresseModell::class),
    JsonSubTypes.Type(AdresseRefModell::class),
    JsonSubTypes.Type(PostboksadresseModell::class)
)
interface AdresseModell {
    val fom: LocalDate?
    val tom: LocalDate?
    val endringstype: Endringstype?
    val endringstidspunkt: LocalDate?
    val adresseType: AdresseType
    val land: Landkode?

    fun clone(fom: LocalDate?, tom: LocalDate?): AdresseModell
}
