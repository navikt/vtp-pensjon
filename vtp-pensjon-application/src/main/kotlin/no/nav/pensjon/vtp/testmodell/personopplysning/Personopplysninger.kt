package no.nav.pensjon.vtp.testmodell.personopplysning

data class Personopplysninger(
    val søker: PersonModell,
    val annenPart: PersonModell?,
    var familierelasjoner: List<FamilierelasjonModell>,
    val familierelasjonerAnnenPart: List<FamilierelasjonModell> = emptyList(),
    val familierelasjonerBarn: List<FamilierelasjonModell> = emptyList(),
    val foreldre: List<PersonModell>? = emptyList()
)
