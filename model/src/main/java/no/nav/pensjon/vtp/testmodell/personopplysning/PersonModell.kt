package no.nav.pensjon.vtp.testmodell.personopplysning

import no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder
import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.kodeverk.Personstatuser.BOSA
import no.nav.pensjon.vtp.testmodell.kodeverk.Sivilstander.UGIF
import no.nav.pensjon.vtp.testmodell.medlemskap.MedlemskapModell
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.util.*
import java.util.Locale.getDefault
import java.util.Optional.ofNullable

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
        val språk: String?,
        val kjønn: Kjønn?,
        val gjeldendeAdresseType: AdresseType?,
        val geografiskTilknytning: GeografiskTilknytningModell?,
        val statsborgerskap: List<StatsborgerskapModell>?,
        val sivilstand: List<SivilstandModell>?,
        val personstatus: List<PersonstatusModell>?,
        val adresser: List<AdresseModell>,
        val medlemskap: MedlemskapModell?
) {
    fun getAdresse(adresseType: AdresseType): Optional<AdresseModell> {
        return ofNullable(adresser.firstOrNull { it.adresseType == adresseType })
    }

    fun getAdresser(type: AdresseType): List<AdresseModell> {
        return adresser.filter { a: AdresseModell -> a.adresseType == type }
    }

    fun getGjeldendeadresseType(): String? {
        return if (gjeldendeAdresseType != null && getAdresse(gjeldendeAdresseType).isPresent) {
            // hvis satt bruk det
            gjeldendeAdresseType.getTpsKode(getAdresse(gjeldendeAdresseType).get().land)
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
        return if (sivilstand == null || sivilstand.isEmpty()) SivilstandModell(UGIF) else sivilstand[0]
    }

    fun getPersonstatusFoo(): PersonstatusModell {
        return if (personstatus == null || personstatus.isEmpty()) PersonstatusModell(BOSA) else personstatus[0]
    }

    fun getStatsborgerskapFoo(): StatsborgerskapModell {
        return if (statsborgerskap == null || statsborgerskap.isEmpty()) StatsborgerskapModell(Landkode.NOR) else statsborgerskap[0]
    }

    fun getSpråk2Bokstaver(): String? {
        return språk?.substring(0, 2)?.toUpperCase(getDefault()) ?: NB
    }
}
