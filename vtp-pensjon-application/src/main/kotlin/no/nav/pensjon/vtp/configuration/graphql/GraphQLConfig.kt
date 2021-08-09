package no.nav.pensjon.vtp.configuration.graphql

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import no.nav.pensjon.vtp.configuration.graphql.model.*
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.io.File
import javax.annotation.PostConstruct

@Component
class GraphQLConfig(private val personModellRepository: PersonModellRepository) {

    @Bean
    @PostConstruct
    fun graphQL(): GraphQL = GraphQL.newGraphQL(buildSchema()).build()

    private fun buildSchema(): GraphQLSchema {
        val sdl = File("src/main/resources/pdl-schema.graphql")
        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring: RuntimeWiring = RuntimeWiring.newRuntimeWiring()
            .type(
                TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher(
                        "hentPerson", PersonDataFetcher(personModellRepository)
                    )
            )
            .type(
                TypeRuntimeWiring.newTypeWiring("Person")
                    .dataFetcher(
                        "navn", NavnDataFetcher()
                    )
            )
            .type(
                TypeRuntimeWiring.newTypeWiring("Navn")
                    .dataFetcher(
                        "fornavn", FornavnDataFetcher()
                    )
                    .dataFetcher(
                        "mellomnavn", MellomnavnDataFetcher()
                    )
                    .dataFetcher(
                        "etternavn", EtternavnDataFetcher()
                    )
                    .dataFetcher(
                        "forkortetNavn", ForkortetNavnDataFetcher()
                    )
                    .dataFetcher(
                        "metaddata", NavnMetadataDataFetcher()
                    )
            )
            .type(
                TypeRuntimeWiring.newTypeWiring("Metadata")
                    .dataFetcher(
                        "historisk", HistoriskMetadataDataFetcher()
                    )
                    .dataFetcher(
                        "master", MasterMetadataDataFetcher()
                    )
            )
            .build()
        val schemaGenerator = SchemaGenerator()
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
    }
}
