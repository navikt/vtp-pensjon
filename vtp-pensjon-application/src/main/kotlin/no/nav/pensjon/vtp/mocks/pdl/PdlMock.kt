package no.nav.pensjon.vtp.mocks.pdl

import graphql.ExecutionInput
import graphql.GraphQL
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "PDL")
@RequestMapping("/rest/pdl-api")
class PdlMock(private val graphQL: GraphQL) {

    @PostMapping(path = ["", "/graphql"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun pdlRequest(@RequestBody request: PdlRequest): MutableMap<String, Any> =
        ExecutionInput.newExecutionInput()
            .query(request.query)
            .variables(request.variables)
            .build()
            .let { graphQL.execute(it) }
            .toSpecification()
}

data class PdlRequest(val query: String, val variables: Map<String, Any>)
