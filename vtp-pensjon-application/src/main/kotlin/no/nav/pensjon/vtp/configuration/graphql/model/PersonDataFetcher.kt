package no.nav.pensjon.vtp.configuration.graphql.model

import graphql.GraphqlErrorBuilder
import graphql.execution.DataFetcherResult
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.nav.pensjon.vtp.testmodell.personopplysning.*
import java.util.*

class PersonDataFetcher(val personModellRepository: PersonModellRepository) : DataFetcher<Any> {

    override fun get(env: DataFetchingEnvironment): Any =
        with(personModellRepository.findById(env.getArgument("ident"))) {
            if (this == null) {
                return DataFetcherResult.newResult<Any>()
                    .error(GraphqlErrorBuilder.newError(env)
                        .message("Fant ikke person")
                        .extensions(mapOf("code" to "not_found"))
                        .build()
                    ).build()
            }

            val person = Person(
                folkeregisteridentifikator = Folkeregisteridentifikator(env.getArgument("ident")).asList(),
                navn = Navn(
                    fornavn = fornavn,
                    etternavn = etternavn,
                    metadata = Metadata.freg,
                ).asList(),
                foedsel = Foedsel(
                    foedselsdato = fødselsdato,//?.let { Date(it.toEpochDay()) },
                    metadata = Metadata.freg,
                ).asList(),
                doedsfall = Doedsfall(
                    doedsdato = dødsdato?.let { Date(it.toEpochDay()) },
                    metadata = Metadata.freg,
                ).asList(),
                statsborgerskap = statsborgerskap?.map { statsborgerskap ->
                    Statsborgerskap(
                        land = statsborgerskap.land.toString(),
                        gyldigFraOgMed = statsborgerskap.fom?.let { Date(it.toEpochDay()) },
                        gyldigTilOgMed = statsborgerskap.tom?.let { Date(it.toEpochDay()) },
                        metadata = Metadata.freg,
                    )
                } ?: emptyList(),
                bostedsadresse = adresser.mapBostedsadresser(),
                innflyttingTilNorge = innflyttingTilNorge?.map {
                    InnflyttingTilNorge(
                        fraflyttingsland = it.fraflyttingsland,
                        folkeregistermetadata = Folkeregistermetadata(
                            gyldighetstidspunkt = it.folkeregistermetadata.gyldighetstidspunkt,
                            ajourholdstidspunkt = it.folkeregistermetadata.ajourholdstidspunkt
                        ),
                        metadata = Metadata.freg
                    )
                } ?: emptyList(),
                utflyttingFraNorge = utflyttingFraNorge?.map {
                    UtflyttingFraNorge(
                        utflyttingsdato = it.utflyttingsdato,
                        tilflyttingsland = it.tilflyttingsland,
                        metadata = Metadata.freg
                    )
                } ?: emptyList()
            )

            return DataFetcherResult.newResult<Any>().data(person).build()
        }
}

private fun <T : Any> T.asList() = listOf(this)

fun List<AdresseModell>.mapBostedsadresser() =
    filter { it.adresseType == AdresseType.BOSTEDSADRESSE }.mapNotNull {
        when (it) {
            is GateadresseModell -> Bostedsadresse(
                gyldigFraOgMed = it.fom?.atStartOfDay(),
                gyldigTilOgMed = it.tom?.atStartOfDay(),
                vegadresse = Vegadresse(
                    adressenavn = it.gatenavn,
                    husnummer = it.husnummer?.toString(),
                    husbokstav = it.husbokstav,
                    postnummer = it.postnummer
                ),
                angittFlyttedato = it.fom,
                utenlandskAdresse = if (it.land != Landkode.NOR) UtenlandskAdresse(landkode = it.land.toString()) else null,
                metadata = Metadata.freg,
            )
            else -> null
        }
    }
