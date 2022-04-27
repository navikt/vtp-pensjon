package no.nav.pensjon.vtp.configuration.graphql

import graphql.GraphQL
import graphql.scalars.datetime.DateScalar
import graphql.scalars.datetime.DateTimeScalar
import graphql.scalars.java.JavaPrimitives
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import no.nav.pensjon.vtp.configuration.graphql.model.IdenterBolkDataFetcher
import no.nav.pensjon.vtp.configuration.graphql.model.IdenterDataFetcher
import no.nav.pensjon.vtp.configuration.graphql.model.PersonDataFetcher
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GraphQLConfig(
    private val personModellRepository: PersonModellRepository,
) {
    @Bean
    @PostConstruct
    fun graphQL(): GraphQL = GraphQL.newGraphQL(buildSchema()).build()

    private fun buildSchema(): GraphQLSchema = SchemaGenerator().makeExecutableSchema(typeRegistry(), runtimeWiring())

    private fun runtimeWiring() = RuntimeWiring.newRuntimeWiring().apply {
        scalar(DateScalar.INSTANCE)
        scalar(DateTimeScalar.INSTANCE)
        scalar(JavaPrimitives.GraphQLLong)
        type("Query") {
            it.dataFetcher("hentPerson", PersonDataFetcher(personModellRepository))
            it.dataFetcher("hentIdenter", IdenterDataFetcher(personModellRepository))
            it.dataFetcher("hentIdenterBolk", IdenterBolkDataFetcher(personModellRepository))
        }
    }.build()

    private fun typeRegistry() =
        javaClass.getResourceAsStream("/pdl-schema.graphql").use {
            SchemaParser().parse(it)
        }
}
