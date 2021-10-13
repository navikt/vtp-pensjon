package no.nav.pensjon.vtp.configuration.graphql.model

import java.time.LocalDateTime
import java.util.*

data class Foedsel(
    val foedselsaar: Int? = null,
    val foedselsdato: Date? = null,
    val foedeland: String? = null,
    val foedested: String? = null,
    val foedekommune: String? = null,
    val metadata: Metadata
)

data class Doedsfall(
    val doedsdato: Date? = null,
    val metadata: Metadata
)

data class Statsborgerskap(
    val land: String,
    val bekreftelsesdato: Date? = null,
    val gyldigFraOgMed: Date? = null,
    val gyldigTilOgMed: Date? = null,
    val metadata: Metadata
)

data class Navn(
    val fornavn: String,
    val mellomnavn: String? = null,
    val etternavn: String,
    val forkortetNavn: String? = null,
    val gyldigFraOgMed: Date? = null,
    val folkeregistermetadata: Folkeregistermetadata? = null,
    val metadata: Metadata
)

data class Metadata(
    val opplysningsId: String? = null,
    val master: String,
    val endringer: List<Endring>,
    val historisk: Boolean,
) {
    companion object {
        val freg = Metadata(null, "FREG", emptyList(), false)
    }
}

data class Folkeregistermetadata(
    val gyldighetstidspunkt: LocalDateTime? = null,
    val ajourholdstidspunkt: LocalDateTime? = null,
    val opphoerstidspunkt: LocalDateTime? = null,
    val kilde: String? = null,
    val aarsak: String? = null,
    val sekvens: Int? = null,
)

data class Endring(
    val type: EndringsType,
    val registrert: LocalDateTime,
    val registretAv: String,
    val systemkilde: String,
    val kilde: String,
)

enum class EndringsType { OPPRETT, KORRIGER, OPPHOER }

data class Bostedsadresse(
    val angittFlyttedao: Date? = null,
    val gyldigFraOgMed: LocalDateTime? = null,
    val gyldigTilOgMed: LocalDateTime? = null,
    val coAdressenavn: String? = null,
    val vegadresse: Vegadresse? = null,
    val matrikkeladresse: Matrikkeladresse? = null,
    val utenlandskAdresse: UtenlandskAdresse? = null,
    val ukjentBosted: UkjentBosted? = null,
    val folkeregistermetadata: Folkeregistermetadata? = null,
    val metadata: Metadata,
)

data class Vegadresse(
    val matrikkelId: Long? = null,
    val husnummer: String? = null,
    val husbokstav: String? = null,
    val bruksenhetsnummer: String? = null,
    val adressenavn: String? = null,
    val kommunenummer: String? = null,
    val bydelsnummer: String? = null,
    val tilleggsnavn: String? = null,
    val postnummer: String? = null,
    val koordinater: Koordinater? = null,
)

data class Koordinater(
    val x: Float? = null,
    val y: Float? = null,
    val z: Float? = null,
    val kvalitet: Int? = null,
)

data class Matrikkeladresse(
    val matrikkelId: Long? = null,
    val bruksenhetsnummer: String? = null,
    val tilleggsnavn: String? = null,
    val postnummer: String? = null,
    val kommunenummer: String? = null,
    val koordinater: Koordinater? = null,
)

data class UtenlandskAdresse(
    val adressenavnNummer: String? = null,
    val bygningEtasjeLeilighet: String? = null,
    val postboksNummerNavn: String? = null,
    val postkode: String? = null,
    val bySted: String? = null,
    val regionDistriktOmraade: String? = null,
    val landkode: String,
)

data class UkjentBosted(val bostedskommune: String? = null)

data class Kontaktadresse(
    val gyldigFraOgMed: LocalDateTime? = null,
    val gyldigTilOgMed: LocalDateTime? = null,
    val type: KontaktadresseType,
    val coAdressenavn: String? = null,
    val postboksadresse: Postboksadresse? = null,
    val vegadresse: Vegadresse? = null,
    val postadresseIFrittFormat: PostadresseIFrittFormat? = null,
    val utenlandskAdresse: UtenlandskAdresse? = null,
    val utenlandskAdresseIFrittFormat: UtenlandskAdresseIFrittFormat? = null,
    val folkeregistermetadata: Folkeregistermetadata? = null,
    val metadata: Metadata
)

enum class KontaktadresseType {
    Innland,
    Utland
}

data class Postboksadresse(
    val postbokseier: String? = null,
    val postboks: String,
    val postnummer: String? = null,
)

data class PostadresseIFrittFormat(
    val adresselinje1: String? = null,
    val adresselinje2: String? = null,
    val adresselinje3: String? = null,
    val postnummer: String? = null,
)

data class UtenlandskAdresseIFrittFormat(
    val adresselinje1: String? = null,
    val adresselinje2: String? = null,
    val adresselinje3: String? = null,
    val postkode: String? = null,
    val byEllerStedsnavn: String? = null,
    val landkode: String,
)

data class Oppholdsadresse(
    val gyldigFraOgMed: LocalDateTime? = null,
    val gyldigTilOgMed: LocalDateTime? = null,
    val coAdressenavn: String? = null,
    val utenlandskAdresse: UtenlandskAdresse? = null,
    val vegadresse: Vegadresse? = null,
    val matrikkeladresse: Matrikkeladresse? = null,
    val oppholdAnnetSted: String? = null,
    val folkeregistermetadata: Folkeregistermetadata? = null,
    val metadata: Metadata
)
