package no.nav.pensjon.vtp.testmodell.personopplysning

import no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder
import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.kodeverk.Personstatuser.BOSA
import no.nav.pensjon.vtp.testmodell.kodeverk.Sivilstander.UGIF
import no.nav.pensjon.vtp.testmodell.medlemskap.MedlemskapModell
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.Locale.getDefault

private const val NB = "NB"

@Document
data class PersonModell(
    @Id
    val ident: String,
    val aktørIdent: String,
    val fornavn: String,
    val etternavn: String,
    val fødselsdato: LocalDate?,
    val dødsdato: LocalDate?,
    val diskresjonskode: Diskresjonskoder?,
    val egenansatt: Boolean,
    val språk: String?,
    val kjønn: Kjønn?,
    val gjeldendeAdresseType: AdresseType?,
    val geografiskTilknytning: GeografiskTilknytningModell?,
    val statsborgerskap: List<StatsborgerskapModell>?,
    val sivilstand: List<SivilstandModell>?,
    val personstatus: List<PersonstatusModell>?,
    val adresser: List<AdresseModell>,
    val medlemskap: MedlemskapModell?,
    var samboerforhold: List<SamboerforholdModell>,
) {
    fun getAdresse(adresseType: AdresseType): AdresseModell? {
        return adresser.firstOrNull { it.adresseType == adresseType }
    }

    fun getAdresser(type: AdresseType): List<AdresseModell> {
        return adresser.filter { a: AdresseModell -> a.adresseType == type }
    }

    fun getGjeldendeadresseType(): String {
        return if (gjeldendeAdresseType != null && getAdresse(gjeldendeAdresseType) != null) {
            // hvis satt bruk det
            gjeldendeAdresseType.getTpsKode(getAdresse(gjeldendeAdresseType)!!.land)
        } else if (adresser.isNotEmpty()) {
            // plukk første hvis finnes
            val adresseModell: AdresseModell = adresser[0]
            adresseModell.adresseType.getTpsKode(adresseModell.land)
        } else {
            // ellers ukjent
            AdresseType.UKJENT_ADRESSE.getTpsKode(null)
        }
    }

    fun getKjønnKode(): String? {
        return kjønn?.name
    }
    fun getSivilstandFoo(): SivilstandModell {
        return sivilstand?.firstOrNull() ?: SivilstandModell(UGIF)
    }

    fun getPersonstatusFoo(): PersonstatusModell {
        return personstatus?.firstOrNull() ?: PersonstatusModell(BOSA)
    }

    fun getStatsborgerskapFoo(): StatsborgerskapModell? {
        return statsborgerskap?.firstOrNull()
    }

    fun getSpråk2Bokstaver(): String {
        return språk?.substring(0, 2)?.uppercase(getDefault()) ?: NB
    }
}
