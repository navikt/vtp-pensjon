package no.nav.pensjon.vtp.testmodell.personopplysning

data class Personopplysninger(
        val søker: PersonModell,
        val annenPart: PersonModell?,
        val familierelasjoner: List<FamilierelasjonModell>,
        val familierelasjonerAnnenPart: List<FamilierelasjonModell> = emptyList(),
        val familierelasjonerBarn: List<FamilierelasjonModell> = emptyList()
)
