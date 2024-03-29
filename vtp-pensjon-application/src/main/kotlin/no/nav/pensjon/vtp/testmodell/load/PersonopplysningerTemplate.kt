package no.nav.pensjon.vtp.testmodell.load

data class PersonopplysningerTemplate(
    val søker: PersonTemplate,
    val annenPart: PersonTemplate?,
    val foreldre: List<PersonTemplate>?,
    val familierelasjoner: List<FamilierelasjonTemplate>
)
