package no.nav.pensjon.vtp.configuration.graphql

import graphql.GraphQL
import graphql.scalars.datetime.DateScalar
import graphql.scalars.datetime.DateTimeScalar
import graphql.scalars.java.JavaPrimitives
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import no.nav.pensjon.vtp.configuration.graphql.model.*
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GraphQLConfig(
    private val personModellRepository: PersonModellRepository,
    private val resourceLoader: ResourceLoader
) {
    @Bean
    @PostConstruct
    fun graphQL(): GraphQL = GraphQL.newGraphQL(buildSchema()).build()

    private fun buildSchema(): GraphQLSchema {
        val sdl = resourceLoader.getResource("classpath:pdl-schema.graphql").file
        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring: RuntimeWiring = RuntimeWiring.newRuntimeWiring()
            .scalar(DateScalar.INSTANCE)
            .scalar(DateTimeScalar.INSTANCE)
            .scalar(JavaPrimitives.GraphQLLong)
            .type(
                TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher("hentPerson", PersonDataFetcher(personModellRepository))
            )
            .build()
        val schemaGenerator = SchemaGenerator()
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
    }
}
