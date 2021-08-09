package no.nav.pensjon.vtp.mocks.psak

import graphql.GraphQL
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["PDL"])
@RequestMapping("/rest/pdl-api")
class PdlMock(private val graphQL: GraphQL) {

    @PostMapping(path = ["/graphql"])
    fun query(@RequestBody query: String) = graphQL.execute(query).toSpecification()
}
