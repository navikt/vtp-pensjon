package no.nav.pensjon.vtp.mocks.saf.graphql

import graphql.GraphQL
import graphql.scalars.datetime.DateScalar
import graphql.scalars.datetime.DateTimeScalar
import graphql.scalars.java.JavaPrimitives
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GraphQLTjeneste(val journalpostRepository: JournalRepository) {

    @Bean
    @PostConstruct
    fun graphQLSaf(): GraphQL = GraphQL.newGraphQL(buildSchema()).build()


    private fun runtimeWiring() = RuntimeWiring.newRuntimeWiring().apply {
        scalar(DateScalar.INSTANCE)
        scalar(DateTimeScalar.INSTANCE)
        scalar(JavaPrimitives.GraphQLLong)
        type("Query") {
            it.dataFetcher("journalpost", JournalpostDataFetcher(journalpostRepository))
            it.dataFetcher("dokumentoversiktFagsak", DokumentoversiktDataFetcher(journalpostRepository))
        }
    }.build()

    private fun typeRegistry() =
        javaClass.getResourceAsStream("/saf.graphql").use {
            SchemaParser().parse(it)
        }

    private fun buildSchema(): GraphQLSchema = SchemaGenerator().makeExecutableSchema(typeRegistry(), runtimeWiring())


}