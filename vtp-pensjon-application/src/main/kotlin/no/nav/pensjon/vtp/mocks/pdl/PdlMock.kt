package no.nav.pensjon.vtp.mocks.pdl

import graphql.ExecutionInput
import graphql.GraphQL
import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["PDL"])
@RequestMapping("/rest/pdl-api")
class PdlMock(private val graphQL: GraphQL) {

    @PostMapping(path = ["/graphql"])
    fun query(@RequestBody query: String) = graphQL.execute(query).toSpecification()

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun pdlRequest(@RequestBody request: PdlRequest) =
        ExecutionInput.newExecutionInput()
            .query(request.query)
            .variables(request.variables)
            .build()
            .let { graphQL.execute(it) }
            .toSpecification()

    @GetMapping(path = ["/schema"])
    fun schema() = graphQL.graphQLSchema
}

data class PdlRequest(val query: String, val variables: Map<String, Any>)
