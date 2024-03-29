package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder
import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.medlemskap.MedlemskapModell
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseModell
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType
import no.nav.pensjon.vtp.testmodell.personopplysning.InnflyttingTilNorgeModell
import no.nav.pensjon.vtp.testmodell.personopplysning.UtflyttingFraNorgeModell
import java.time.LocalDate

data class PersonTemplate(
    val ident: String,
    val aktørIdent: String?,
    var fornavn: String?,
    var etternavn: String?,
    var fødselsdato: LocalDate?,
    val dødsdato: LocalDate?,
    val diskresjonskode: Diskresjonskoder?,
    val egenansatt: Boolean,
    val språk: String?,
    val kjønn: Kjønn,
    val gjeldendeAdresseType: AdresseType?,
    val geografiskTilknytning: GeografiskTilknytningTemplate?,
    val statsborgerskap: List<StatsborgerskapTemplate>?,
    val sivilstand: List<SivilstandTemplate>?,
    val personstatus: List<PersonstatusTemplate>?,
    val adresser: List<AdresseModell>?,
    val medlemskap: MedlemskapModell?,
    val samboerforhold: List<SamboerforholdTemplate>?,
    val innflyttingTilNorge: List<InnflyttingTilNorgeModell>?,
    val utflyttingFraNorge: List<UtflyttingFraNorgeModell>?
)
