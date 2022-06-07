package no.nav.pensjon.vtp.mocks.pdl

import graphql.ExecutionInput
import graphql.GraphQL
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

    @PostMapping(path = ["", "forpopp/graphql"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun pdlForPoppRequest(@RequestBody request: PdlRequest): ResponseEntity<String> {
        val requestedIdent = request.variables["ident"]
        return ResponseEntity.ok(createPdlResponseForPopp(requestedIdent))
    }

    private fun createPdlResponseForPopp(ident: Any?): String {
        return """
            {
              "data": {
                "hentPerson": {
                  "folkeregisteridentifikator": [
                    {
                      "identifikasjonsnummer": "$ident",
                      "status": "I_BRUK",
                      "type": "FNR",
                      "metadata": {
                        "historisk": false,
                        "master": "FREG"
                      }
                    }
                  ],
                  "foedsel": [
                    {
                      "foedselsaar": 1988,
                      "metadata": {
                        "historisk": false,
                        "master": "FREG",
                        "endringer": []
                      }
                    }
                  ]
                }
              }
            }
        """.trimIndent()
    }
}

data class PdlRequest(val query: String, val variables: Map<String, Any>)
