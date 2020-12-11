package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder
import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.medlemskap.MedlemskapModell
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType
import java.time.LocalDate

data class PersonTemplate(
    val ident: String,
    val aktørIdent: String?,
    var fornavn: String?,
    var etternavn: String?,
    val fødselsdato: LocalDate?,
    val dødsdato: LocalDate?,
    val diskresjonskode: Diskresjonskoder?,
    val språk: String?,
    val kjønn: Kjønn,
    val gjeldendeAdresseType: AdresseType?,
    val geografiskTilknytning: GeografiskTilknytningTemplate?,
    val statsborgerskap: List<StatsborgerskapTemplate>?,
    val sivilstand: List<SivilstandTemplate>?,
    val personstatus: List<PersonstatusTemplate>?,
    val adresser: List<AdresseModell>?,
    val medlemskap: MedlemskapModell?
)
