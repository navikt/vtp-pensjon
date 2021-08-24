package no.nav.pensjon.vtp.auth.isso

data class Name(
    val name: String,
    val value: String
)

data class Callback(
    val type: String,
    val input: List<Name>,
    val output: List<Name>
) {
    constructor(type: String, input: Name, output: Name) : this(type, listOf(input), listOf(output))
}

data class EndUserAuthenticateTemplate(
    val authId: String,
    val template: String,
    val callbacks: List<Callback>,
    val header: String,
    val stage: String
)
